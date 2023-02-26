package tn.esprit.springfever.Services.Implementation;

import com.netflix.discovery.shared.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.DTO.Job_RDV_DTO;
import tn.esprit.springfever.Services.Interfaces.IJobRDV;
import tn.esprit.springfever.Services.Interfaces.JobMapper;
import tn.esprit.springfever.entities.*;
import tn.esprit.springfever.repositories.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    /*public Job_RDV updateJobRDV(Long ID_Job_DRV, Job_RDV_DTO jobRdvDto) {
        Job_RDV jobRdv = jobRdvRepository.findById(ID_Job_DRV).orElse(null);
        if (jobRdv != null) {
            jobMapper.updateClaimFromDto(jobRdv, jobRdvDto);
            log.info(jobRdv.getSalle_Rdv());
            jobRdvRepository.save(jobRdv);
            log.info("Job was successfully updated !");
            return jobRdv;
        }
        log.info("Job not found !");
        return  jobRdv;
    }*/

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

    public String AssignEntretienToRDV(Long ID_Job_Entretien, Long ID_Job_DRV) {
        Entretien entretien = entretienRepository.findById(ID_Job_Entretien).orElse(null);
        Job_RDV jobRdv = jobRdvRepository.findById(ID_Job_DRV).orElse(null);
        if (entretien != null && jobRdv != null) {
            jobRdv.setEntretien(entretien);
            jobRdvRepository.save(jobRdv);
            return "Entrtien is Affeced To RDV";
        }
        return "Entretien Or Job RDV Are not found";
    }

    public String AssignJobApplicationToRDV(Long Id_Job_Application, Long ID_Job_DRV) {
        Job_Application job_application = jobApplicationRepository.findById(Id_Job_Application).orElse(null);
        Job_RDV jobRdv = jobRdvRepository.findById(ID_Job_DRV).orElse(null);
        if (job_application != null && jobRdv != null) {
            jobRdv.setJobApplication(job_application);
            jobRdvRepository.save(jobRdv);
            return "Added";
        }
        return "Job RDV OR Job Application are not found ! ";
    }

    public String AssignCandidateToJobRDV(Long ID_Job_DRV, Long Id_Job_Application) {
        Job_RDV jobRdv = jobRdvRepository.findById(ID_Job_DRV).orElse(null);
        Job_Application job_application = jobApplicationRepository.findById(Id_Job_Application).orElse(null);
        if (jobRdv != null && job_application != null) {
            jobRdv.setCandidate(job_application.getUser());
            jobRdvRepository.save(jobRdv);
            return "Candidate is affected to JOB RDV with sucess ! ";
        }
        return "Candidate or JOB RDV are not founf !";
    }

    public String AssignJuryToJobRDV(Long ID_Job_DRV, Long id) {
        Job_RDV jobRdv = jobRdvRepository.findById(ID_Job_DRV).orElse(null);
        User jury = userRepository.findById(id).orElse(null);
        if (jobRdv != null && jury != null) {
            jobRdv.setJury(jury);
            jobRdvRepository.save(jobRdv);
            return "Jury is assigned to Job Rdv sucessffully";
        }
        return "Job Rdv Or Jury are not found ! ";

    }

    public List<LocalDateTime> getDisponibilites(Long userId) {
        // Récupération de l'utilisateur
        User user = userRepository.findById(userId).orElse(null);
        Long id = user.getId();

        // Récupération des disponibilités
        List<Disponibilites> disponibilites = disponiblitiesRepository.findByUserId(id);

        // Conversion en liste de paires de dates
        List<LocalDateTime> dateRanges = new ArrayList<>();
        for (Disponibilites dispo : disponibilites) {
            dateRanges.add( dispo.getEnd_date());
            dateRanges.add( dispo.getEnd_date());
        }

        return dateRanges;
    }
    public Disponibilites AddDispo(Disponibilites disponibilites) {
        return disponiblitiesRepository.save(disponibilites);
    }
    public String AssignUserToDisponibilities(Long idDispo, Long idUser ){
        User user=userRepository.findById(idUser).orElse(null);
        Disponibilites disponibilites=disponiblitiesRepository.findById(idDispo).orElse(null);
        if(user!=null && disponibilites!=null){
            disponibilites.setUser(user);
            disponiblitiesRepository.save(disponibilites);
            return "OK ";
        }
        return "Not OK";
    }
    /*public LocalDateTime findFirstAvailableDateTime(Long dispoCandidate, Long dispoJury,
                                                    int interviewDuration) {
        Disponibilites disponiblityCandidate=disponiblitiesRepository.findById(dispoCandidate).orElse(null);
        Disponibilites disponiblityJury=disponiblitiesRepository.findById(dispoJury).orElse(null);
        LocalDateTime candidatePreferredDateTime=disponiblityCandidate.getPreferDateTime();

        LocalDateTime juryPreferredDateTime=disponiblityJury.getPreferDateTime();

        List<LocalDateTime>  candidateAvailability=getDisponibilites(disponiblityCandidate.getUser().getId());
        List<LocalDateTime> juryAvailability=getDisponibilites(disponiblityJury.getUser().getId());

        // Trouver la première plage horaire disponible qui est suffisamment longue pour l'entretien
        for (LocalDateTime candidateStart : candidateAvailability) {
            LocalDateTime candidateEnd = candidateStart.plusMinutes(interviewDuration);
            if (candidateAvailability.contains(candidateStart) && candidateAvailability.contains(candidateEnd)) {
                for (LocalDateTime juryStart : juryAvailability) {
                    LocalDateTime juryEnd = juryStart.plusMinutes(interviewDuration);
                    if (juryAvailability.contains(juryStart) && juryAvailability.contains(juryEnd)) {

                        // Vérifier que la plage horaire correspond aux préférences du candidat et du jury
                        if (candidateStart.isEqual(candidatePreferredDateTime) && juryStart.isEqual(juryPreferredDateTime)) {
                            return candidateStart;
                        }
                    }
                }
            }
        }

        // Si aucune plage horaire n'est trouvée, retourner null
        List<LocalDateTime> possibleStartTimes = new ArrayList<>();

        for (LocalDateTime juryStart : juryAvailability) {
            LocalDateTime juryEnd = juryStart.plusMinutes(interviewDuration);
            if (juryAvailability.contains(juryStart) && juryAvailability.contains(juryEnd)) {
                possibleStartTimes.add(juryStart);
            }
        }
        if (possibleStartTimes.isEmpty()) {

            return juryPreferredDateTime;
        }

// Sélectionner une plage horaire aléatoire parmi les disponibilités du jury
        Random rand = new Random();
        int randomIndex = rand.nextInt(possibleStartTimes.size());
        return possibleStartTimes.get(randomIndex);

    }*/


    public LocalDateTime findFirstAvailableDateTime(Long dispoCandidate, Long dispoJury, int interviewDuration) {
        Disponibilites disponiblityCandidate = disponiblitiesRepository.findById(dispoCandidate).orElse(null);
        Long idCandadte = disponiblityCandidate.getUser().getId();
        Job_RDV jobRdv=jobRdvRepository.findJob_RDVByCandidate_Id(idCandadte);
        Disponibilites disponiblityJury = disponiblitiesRepository.findById(dispoJury).orElse(null);
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




}
