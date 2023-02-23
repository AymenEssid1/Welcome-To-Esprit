package tn.esprit.springfever.Services.Implementation;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.springfever.Services.Interfaces.IJobApplication;
import tn.esprit.springfever.entities.Image_JobOffer;
import tn.esprit.springfever.entities.Job_Application;
import tn.esprit.springfever.entities.Job_Offer;
import tn.esprit.springfever.entities.User;
import tn.esprit.springfever.repositories.JobApplicationRepository;
import tn.esprit.springfever.repositories.JobApplicatonPdfRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class JobApplicationService implements IJobApplication {

    @Autowired
    JobApplicationRepository jobApplicationRepository;
    @Autowired
    JobApplicatonPdfRepository jobApplicatonPdfRepository;

    @Autowired
    private JavaMailSender mailSender;


    public Job_Application AddJobApplication (Job_Application job_application){
       return jobApplicationRepository.save(job_application);

    }
    public Job_Application save(byte[] cv, byte[] lettre, String imageName) throws Exception {
        //String location = jobApplicatonPdfRepository.save(cv,lettre,imageName);
        return jobApplicationRepository.save(new Job_Application(cv, lettre));
    }


    public List<Job_Application> GetAllJobApplications(){
        return jobApplicationRepository.findAll();

    }

    public String DeleteJobApplication (Long Id_Job_Application){
        Job_Application JobApplicationExisted=jobApplicationRepository.findById(Id_Job_Application).orElse(null);
        if(JobApplicationExisted!=null){
            jobApplicationRepository.delete(JobApplicationExisted);
            return "Job Application is Deleted !";
        }
        return "Job Application Does not Exist";

    }

    /*public Job_Application UpdateJobApplication(Long Id_Job_Application , Job_Application job_application){
        Job_Application JobApplicationExisted=jobApplicationRepository.findById(Id_Job_Application).orElse(null);
        if(JobApplicationExisted!=null){
            JobApplicationExisted.setJobOffer(job_application.getJobOffer());
            JobApplicationExisted.setCv(job_application.getCv());
            JobApplicationExisted.setUser(job_application.getUser());
            JobApplicationExisted.setRdv(job_application.getRdv());
            JobApplicationExisted.setLettreMotivation(job_application.getLettreMotivation());
            return jobApplicationRepository.save(JobApplicationExisted);
        }
        log.info("Job Application does not exist ! ");
        return JobApplicationExisted;

    }*/

    public Job_Application savef(byte[] cv, byte[] lettre, String location_Cv, String location_LettreMotivation) throws Exception {
        Path cvFile = Paths.get("C:\\Users\\User\\Desktop\\" + new Date().getTime() + "-" + location_Cv);
        Path lettreFile = Paths.get("C:\\Users\\User\\Desktop\\" + new Date().getTime() + "-" + location_LettreMotivation);

        Files.write(cvFile, cv);
        Files.write(lettreFile, lettre);

        String cvLocation = cvFile.toAbsolutePath().toString();
        String lettreLocation = lettreFile.toAbsolutePath().toString();

        return jobApplicationRepository.save(new Job_Application( cvLocation, lettreLocation));
    }

    public FileSystemResource findCV(Long Id_Job_Application) {

        Job_Application job_application = jobApplicationRepository.findById(Id_Job_Application)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return jobApplicatonPdfRepository.findInFileSystem(job_application.getLocation_Cv());
    }

    public FileSystemResource findLettreMotivation(Long Id_Job_Application) {

        Job_Application job_application = jobApplicationRepository.findById(Id_Job_Application)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return jobApplicatonPdfRepository.findInFileSystem(job_application.getLocation_LettreMotivation());
    }

    public String FilterCv(Long Id_Job_Application){
        Job_Application job_application=jobApplicationRepository.findById(Id_Job_Application).orElse(null);
        Job_Offer job_offer=job_application.getJobOffer();
        String text= extractTextFromPdf(Id_Job_Application);
        String text2=job_offer.getSubject();
        System.out.println(text);
        System.out.println(text2);
        if(text.contains(text2)) {
            System.out.println("Text contains Text2");
            return "Text contains Text2";
        } else {
            System.out.println("Text does not contain Text2");
            return "Text does not contain Text2";
        }

    }

    public String extractTextFromPdf(Long id){
        String text = null;
        try {
            FileSystemResource fileSystemResource = findLettreMotivation(id);

            PdfReader reader = new PdfReader(fileSystemResource.getPath());

            int n = reader.getNumberOfPages();
            text = "";
            for (int i = 0; i < n; i++) {
                text += PdfTextExtractor.getTextFromPage(reader, i + 1).trim() + "\n";
            }
            reader.close();
            System.out.println(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }


    public void sendEmail(Long id, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("chaima.dammak@espri.tn");
        Job_Application job_application=jobApplicationRepository.findById(id).orElse(null);
        String to=job_application.getUser().getEmail();
        System.out.println(to);
        message.setTo(to);
        message.setSubject(subject);
        //body="Hello !! ";
        message.setText(body);
        mailSender.send(message);
    }
    }






