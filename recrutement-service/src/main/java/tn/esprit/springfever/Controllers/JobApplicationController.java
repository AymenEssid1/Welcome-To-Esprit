package tn.esprit.springfever.Controllers;


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
import tn.esprit.springfever.Services.Interfaces.IJobApplication;
import tn.esprit.springfever.entities.Job_Application;
import tn.esprit.springfever.repositories.JobApplicationRepository;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


@RestController
@RequestMapping("/Job")
public class JobApplicationController {
    @Autowired
    IJobApplication iJobApplication;
    @Autowired
    JobApplicationRepository jobApplicationRepository;

    /*@PostMapping(value="addJobApplication/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void AddJobApplication (@RequestPart("job_application") Job_Application job_application,
                                              @RequestPart("cv") MultipartFile cvFile,
                                              @RequestPart("lettreMotivation") MultipartFile lettreMotivationFile) {
        byte[] cv = null;
        byte[] lettreMotivation = null;
        try {
            cv = cvFile.getBytes();
            lettreMotivation = lettreMotivationFile.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        job_application.setCv(cv);
        job_application.setLettreMotivation(lettreMotivation);

    }*/
    /*@PostMapping(value = "/JobAdd" ,  consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<Job_Application> uploadImage(@RequestParam("cv") MultipartFile cvFile, @RequestPart("lettreMotivation") MultipartFile lettreMotivationFile) {
        try {
            Job_Application savedImageData = iJobApplication.save(cvFile.getBytes(),lettreMotivationFile.getBytes(),cvFile.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedImageData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/


    @PostMapping(value = "/JobAdd2" ,  consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<Job_Application> uploadImage(@RequestParam("cv") MultipartFile cvFile,@RequestParam("lettreMotivation") MultipartFile lettreMotivationFile ) {
        try {
            Job_Application savedImageData = iJobApplication.savef(cvFile.getBytes(), lettreMotivationFile.getBytes(),cvFile.getOriginalFilename(),lettreMotivationFile.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedImageData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }







    @GetMapping(value = "/GetCV/{id}")
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
    }





    @GetMapping("getAllJobApplications/")
    public List<Job_Application> GetAllJobApplications() {
        return iJobApplication.GetAllJobApplications();

    }

    /*@PutMapping("updateJobApplication/{id}")
    public Job_Application UpdateJobApplication(@PathVariable("id") Long Id_Job_Application , @RequestBody Job_Application job_application){
        return iJobApplication.UpdateJobApplication(Id_Job_Application,job_application);

    }*/


    @DeleteMapping("deleteJobApplication/{id}")
    public String DeleteJobApplication(@PathVariable("id") Long Id_Job_Application) {
        return iJobApplication.DeleteJobApplication(Id_Job_Application);

    }


    @GetMapping(value = "/pdf-text/{id}")
    public String extractTextFromPdf(@PathVariable("id") Long Id) {
        return iJobApplication.extractTextFromPdf(Id);
    }


    @GetMapping(value = "/FilterCv/{id}")
    public String FilterCv(@PathVariable("id") Long Id_Job_Application){
         return iJobApplication.FilterCv(Id_Job_Application);

    }

    @PostMapping("/send-email/{id}")
    public ResponseEntity<String> sendEmail(@PathVariable ("id")Long id) {


        String subject = "Test Email";
        String text = "This is a test email sent from my Spring Boot application.";

        iJobApplication.sendEmail(id,subject,text);

        return ResponseEntity.ok("Email sent successfully!");
    }
}

