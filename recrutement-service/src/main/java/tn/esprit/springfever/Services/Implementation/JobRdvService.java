package tn.esprit.springfever.Services.Implementation;



import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.Configurations.TwilioConfig;
import tn.esprit.springfever.DTO.Job_RDV_DTO;
import tn.esprit.springfever.Services.Interfaces.IJobRDV;
import tn.esprit.springfever.Services.Interfaces.JobMapper;
import tn.esprit.springfever.entities.*;
import tn.esprit.springfever.enums.RDV_Type;
import tn.esprit.springfever.repositories.*;


import java.time.LocalDateTime;
import java.util.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;




@Service
@Slf4j
public class JobRdvService implements IJobRDV {
    @Autowired
    JobRdvRepository jobRdvRepository;
    @Autowired
    EntretienRepository entretienRepository;

    @Autowired
    JobApplicationRepository jobApplicationRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    DisponiblitiesRepository disponiblitiesRepository;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    JobMapper jobMapper;
    @Autowired
    private TwilioConfig twilioConfig;


    public Job_RDV addJobRDV(Job_RDV job_rdv) {
        return jobRdvRepository.save(job_rdv);

    }

    public List<Job_RDV> getAllJobRDVs() {
        return jobRdvRepository.findAll();

    }
    /*public Job_RDV updateJobRDV (Long ID_Job_DRV , Job_RDV job_rdv ) {
        Job_RDV jobRdvExisted = jobRdvRepository.findById(ID_Job_DRV).orElse(null);
        if(jobRdvExisted!=null){
            jobRdvExisted.setUser2(job_rdv.getUser2());
            jobRdvExisted.setUser(job_rdv.getUser());
            jobRdvExisted.setEntretien(job_rdv.getEntretien());
            jobRdvExisted.setType_RDV(job_rdv.getType_RDV());
            jobRdvExisted.setSalle_Rdv(job_rdv.getSalle_Rdv());
            jobRdvExisted.setJobApplication(job_rdv.getJobApplication());
            jobRdvRepository.save(jobRdvExisted);
            log.info("Job Offer is updated !");
            return jobRdvExisted ;

        }
        log.info("Job RDV does not exist !! ");
        return jobRdvExisted;
    }*/

    public Job_RDV updateJobRDV(Long ID_Job_DRV, Job_RDV_DTO jobRdvDto) {
        Job_RDV jobRdv = jobRdvRepository.findById(ID_Job_DRV).orElse(null);
        if (jobRdv != null) {
            jobMapper.updateClaimFromDto(jobRdvDto,jobRdv );
            log.info(jobRdv.getSalle_Rdv());
            jobRdvRepository.save(jobRdv);
            log.info("Job was successfully updated !");
            return jobRdv;
        }
        log.info("Job not found !");
        return  jobRdv;
    }

    public String deleteJobOffer(Long ID_Job_DRV) {
        Job_RDV jobRdv = jobRdvRepository.findById(ID_Job_DRV).orElse(null);
        if (jobRdv != null) {
            jobRdvRepository.delete(jobRdv);
            log.info("Job RDV Is Deleted With Success ! ");
            return "Job RDV Is Deleted With Success ! ";
        }
        log.info("Job RDV Does not Exist !!");
        return "Job RDV Does not Exist !!";

    }



    public String AssignJobApplicationAndJuryToRDV(Long Id_Job_Application, Long ID_Job_DRV, Long idJury) {
        Job_Application job_application = jobApplicationRepository.findById(Id_Job_Application).orElse(null);
        User jury = userRepository.findById(idJury).orElse(null);
        Job_RDV jobRdv = jobRdvRepository.findById(ID_Job_DRV).orElse(null);
        if (job_application != null && jobRdv != null &&jury!=null) {
            jobRdv.setJobApplication(job_application);
            jobRdv.setJury(jury);
            jobRdvRepository.save(jobRdv);
            return "JobApplication And Jury are affected To JobRDV";
        }
        return "Job RDV OR Job Application OR Jury  are not found ! ";
    }






    public LocalDateTime findFirstAvailableDateTime(Long dispoCandidate, Long dispoJury, int interviewDuration) {
        Disponibilites disponiblityCandidate = disponiblitiesRepository.findById(dispoCandidate).orElse(null);
        Disponibilites disponiblityJury = disponiblitiesRepository.findById(dispoJury).orElse(null);
        Long idJury =disponiblityJury.getJobRDV().getJury().getId();
        Job_RDV jobRdv = jobRdvRepository.findJob_RDVByJury_Id(idJury);
        LocalDateTime candidatePreferredDateTime = disponiblityCandidate.getPreferDateTime();
        LocalDateTime juryPreferredDateTime = disponiblityJury.getPreferDateTime();

        List<LocalDateTime> candidateAvailability = Arrays.asList(disponiblityCandidate.getStart_date(), disponiblityCandidate.getEnd_date());
        List<LocalDateTime> juryAvailability = Arrays.asList(disponiblityJury.getStart_date(), disponiblityJury.getEnd_date());

        // Trouver la première plage horaire disponible qui est suffisamment longue pour l'entretien
        LocalDateTime firstAvailableDateTime = null;
        for (LocalDateTime candidateStart : candidateAvailability) {
            LocalDateTime candidateEnd = candidateStart.plusMinutes(interviewDuration);
            if (candidateAvailability.contains(candidateStart) && candidateAvailability.contains(candidateEnd)) {
                for (LocalDateTime juryStart : juryAvailability) {
                    LocalDateTime juryEnd = juryStart.plusMinutes(interviewDuration);
                    if (juryAvailability.contains(juryStart) && juryAvailability.contains(juryEnd)) {

                        // Vérifier que la plage horaire correspond aux préférences du candidat et du jury
                        if (candidateStart.isEqual(candidatePreferredDateTime) && juryStart.isEqual(juryPreferredDateTime)) {
                            jobRdv.setAppointmentDate(candidateStart);
                            jobRdvRepository.save(jobRdv);
                            return candidateStart;
                        }

                        // Si c'est la première plage horaire disponible pour l'entretien, la sauvegarder
                        if (firstAvailableDateTime == null || juryStart.isBefore(firstAvailableDateTime)) {
                            firstAvailableDateTime = juryStart;
                        }
                    }
                }
            }
        }

        // Si aucune plage horaire disponible n'est trouvée, retourner la première plage horaire disponible du jury
        if (firstAvailableDateTime != null) {
            jobRdv.setAppointmentDate(firstAvailableDateTime);
            jobRdvRepository.save(jobRdv);
            return firstAvailableDateTime;

        } else {
            jobRdv.setAppointmentDate(juryAvailability.get(0));
            jobRdvRepository.save(jobRdv);
            return juryAvailability.get(0);
        }
    }

    public String generateJitsiMeetLink(Long id) {
        Job_RDV jobRdv = jobRdvRepository.findById(id).orElse(null);
        if (jobRdv.getType_RDV() == RDV_Type.ONLIGNE) {
            String roomName = "my-room-name"; // Remplacez "my-room-name" par un nom de salle de réunion valide.
            String domain = "meet.jit.si"; // Remplacez "meet.jit.si" par le nom de domaine Jitsi Meet de votre choix.

            String link = "https://" + domain + "/" + roomName;
            return link;

        }
        return "RDV n est pas en ligne ";

    }



    public double calculateDistance(Long idJobApplication) {
        Job_Application job_application = jobApplicationRepository.findById(idJobApplication).orElse(null);
        double latitude1 = job_application.getLatitudeCandidate();
        double longitude1 = job_application.getLongitudeCandidate();

        String address = "Ariana,2083";
        String url = "https://nominatim.openstreetmap.org/search?q=" + address + "&format=json&addressdetails=1";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String response = EntityUtils.toString(httpEntity);
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            double latitude2 = Double.parseDouble(jsonObject.getString("lat"));
            double longitude2 = Double.parseDouble(jsonObject.getString("lon"));

            double earthRadius = 6371; // km

            double dLat = Math.toRadians(latitude2 - latitude1);
            double dLon = Math.toRadians(longitude2 - longitude1);
            double lat1 = Math.toRadians(latitude1);
            double lat2 = Math.toRadians(latitude2);

            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double distance = earthRadius * c;

            return distance;
        } catch (Exception ex) {
            ex.printStackTrace();
            // handle error
            System.out.println("Not Ok HERE ");
        }
        return -1;
    }

    public void sendEmailToFIXRDV(Long id, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("chaima.dammak@espri.tn");
        Job_Application job_application = jobApplicationRepository.findById(id).orElse(null);
        String to = job_application.getUser().getEmail();
        System.out.println(to);
        message.setTo(to);
        message.setSubject(subject);
        //body="Hello !! ";
        message.setText(body);
        mailSender.send(message);
    }

    public void FixationRDV(Long id) {
        Job_RDV jobRdv = jobRdvRepository.findById(id).orElse(null);
        Long idJobApplication=jobRdv.getJobApplication().getId_Job_Application();
        double distance = calculateDistance(idJobApplication);
        RDV_Type rdvType = distance > 100.0 ? RDV_Type.ONLIGNE : RDV_Type.FACE_TO_FACE;
        jobRdv.setType_RDV(rdvType);
        jobRdvRepository.save(jobRdv);
        if (jobRdv.getType_RDV() == RDV_Type.ONLIGNE) {
            String subject = "Invitation to online interview for Job  position";
            String body = "Dear Candidate ,\n" + "\n" +
                    "\n" +
                    "I hope this email finds you well. I am writing to invite you to an online interview for the" + jobRdv.getJobApplication().getJobOffer().getTitle() + "position at ESPRIT.\n" +
                    "\n" +
                    "\"As our team has taken into consideration the fact that you are located more than 100km away," + "\n" +
                    "we have decided to conduct the interview online." + "\n" +
                    " Your interview is scheduled for" + jobRdv.getAppointmentDate() + " and we kindly ask that you join the meeting room by clicking on the following link: \n" + generateJitsiMeetLink(id) +
                    "\n Please ensure that you have a stable internet connection and a webcam for the interview.\n"
                    + "\n \n \n" + "Best Reagrds,";
            Long idJobApplication1 = jobRdv.getJobApplication().getId_Job_Application();
            sendEmailToFIXRDV(idJobApplication1, subject, body);

        } else {
            String subject = "Invitation to FaceToFace interview for Job  position";
            String body = "Dear Candidate ,\n" + "\n" +
                    "\n" +
                    "I hope this email finds you well. I am writing to invite you to an online interview for the" + jobRdv.getJobApplication().getJobOffer().getTitle() + "position at ESPRIT.\n" +
                    "\n" + "Your interview is scheduled for " + jobRdv.getAppointmentDate() + "\n at ESPRIT in " + jobRdv.getSalle_Rdv() + "\n"
                    + "\n Best Reagrds,!";
            Long idJobApp = jobRdv.getJobApplication().getId_Job_Application();
            sendEmailToFIXRDV(idJobApp, subject, body);
        }

    }

    public void sendReminderSMS(Job_RDV rdv) {
        // Get candidate phone number from rdv object
        String candidatePhoneNumber = "+216 29 541 816";

        // Set reminder message
        String message = "Reminder: Your job interview is scheduled tomorrow at " + rdv.getAppointmentDate().toString() + ". Don't forget to bring your resume and portfolio.";

        // Send SMS using Twilio API
        Message.creator(new PhoneNumber(candidatePhoneNumber), new PhoneNumber(twilioConfig.getTwilioNumber()), message).create();
    }


}

