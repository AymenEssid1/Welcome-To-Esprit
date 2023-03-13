package tn.esprit.springfever.Controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.Services.Interfaces.IDisponibilites;
import tn.esprit.springfever.Services.Interfaces.IJobApplication;
import tn.esprit.springfever.entities.Disponibilites;
import tn.esprit.springfever.entities.Job_Application;
import tn.esprit.springfever.repositories.JobApplicationRepository;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.List;


@RestController
@RequestMapping("/Job")
public class JobApplicationController {
    @Autowired
    IJobApplication iJobApplication;
    @Autowired
    JobApplicationRepository jobApplicationRepository;
    @Autowired
    IDisponibilites iDisponibilites;






    @PostMapping(value = "/AddJobApplication" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<String> addJobApplicationAndSendEmail(@RequestParam("cv") MultipartFile cvFile,
                                                                @RequestParam("lettreMotivation") MultipartFile lettreMotivationFile,
                                                                @RequestParam("Id_Job_Offer") Long idJobOffer,
                                                                @RequestParam("idUser") Long idUser,
                                                                @RequestParam("address") String address
    ) {
        try {
            //Ajouter la Candidature
            Job_Application jobApplication = iJobApplication.savef(cvFile.getBytes(), lettreMotivationFile.getBytes(),
                    cvFile.getOriginalFilename(), lettreMotivationFile.getOriginalFilename());
            iJobApplication.AssignJobOfferAndCandidateToJobApplication(idJobOffer,jobApplication.getId_Job_Application(),idUser,address);

            //Envoyer Mail aprés le filtrage du CV
            if (iJobApplication.FilterCv(jobApplication.getId_Job_Application()) == true) {
                String subject = "Answer on the Job offer";
                String text = " Dear Candidate , \n" + "Congratulations \n" +
                        " We are pleased to inform you that your CV has been accepted for the position  at our company.\n " +
                        "We would like to congratulate you on your selection and thank you for your interest in working with us.\n" +
                        "We will be contacting you shortly to schedule an interview and discuss the next steps in the hiring process.\n" +
                        " Please be prepared to provide us with any additional information or documentation that we may require."
                        + "We look forward to meeting with you and discussing your qualifications further."
                        + "Sincerely!";

                iJobApplication.sendEmail(jobApplication.getId_Job_Application(), subject, text);
            } else {
                String subject = "Answer on the Job offer";
                String text = "Dear Candidate \n " +
                        "Thank you for your interest in the job opportunity at our company. After carefully reviewing your application, " +
                        "we regret to inform you that your CV does not meet our current requirements for the position.\n" + "Please note that this decision is in no way a reflection of your qualifications or experience, " +
                        "and we encourage you to apply for future positions that match your skills\n"
                        + "We appreciate the time and effort you put into your application and wish you all the best in your job search.\n"
                        + "Sincerely!";

                iJobApplication.sendEmail(jobApplication.getId_Job_Application(), subject, text);
            }

            String result = iJobApplication.AssignJobOfferAndCandidateToJobApplication(idJobOffer, jobApplication.getId_Job_Application(), idUser, address);

            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }











    /*@GetMapping(value = "/GetCV/{id}")
    public ResponseEntity<FileSystemResource> downloadPDFCV(@PathVariable("id") Long Id) {
        try {
            System.out.println("mriguel");
            FileSystemResource fileSystemResource = iJobApplication.findCV(Id);
            System.out.println("mriguel");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(fileSystemResource);
        } catch (Exception e) {
            System.out.println("eror pdf");

            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping(value = "/GetLettreMotivation/{id}")
    public ResponseEntity<FileSystemResource> downloadPDFLettreMotivation(@PathVariable("id") Long Id) {
        try {
            System.out.println("mriguel");
            FileSystemResource fileSystemResource = iJobApplication.findLettreMotivation(Id);
            System.out.println("mriguel");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(fileSystemResource);
        } catch (Exception e) {
            System.out.println("eror pdf");

            return ResponseEntity.notFound().build();
        }
    }*/

    @GetMapping("getAllJobApplications/")
    public List<Job_Application> GetAllJobApplications() {
        return iJobApplication.GetAllJobApplications();

    }




    @DeleteMapping("deleteJobApplication/{id}")
    public String DeleteJobApplication(@PathVariable("id") Long Id_Job_Application) {
        return iJobApplication.DeleteJobApplication(Id_Job_Application);

    }



    /*@GetMapping(value = "/pdf-text-testNLP/{id}")
    public String extractTextFromPdf2(@PathVariable("id") Long Id) throws IOException {
        return iJobApplication.extractTextFromPdf2(Id);
    }*/


    @GetMapping(value = "/FilterCv/{id}")
    public Boolean FilterCv(@PathVariable("id") Long Id_Job_Application){
        return iJobApplication.FilterCv(Id_Job_Application);

    }






    /*@GetMapping(value = "/FilterCvCompetences/{id}")
    public String extractSkills(@PathVariable("id") Long id) throws IOException{
        return iJobApplication.extractSkills(id);

    }*/
    @GetMapping("StatNbApplicationByJob_Offer/")
    public String countApplicationsByJobOffer() {
        List<Object[]> jobOfferApplicationCount = iJobApplication.countApplicationsByJobOffer();
        String ch = "";
        for (Object[] result : jobOfferApplicationCount) {
            Long id = ((BigInteger) result[0]).longValue();
            String title = (String) result[1];
            String subject = (String) result[2];
            BigInteger count = (BigInteger) result[3];
            ch = ch + ("Offer " + id + " (" + title + " - " + subject + ") received " + count + " applications.\n");
        }
        return ch;
    }


}

