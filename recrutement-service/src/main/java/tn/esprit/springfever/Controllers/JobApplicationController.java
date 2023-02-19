package tn.esprit.springfever.Controllers;


import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
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
    @PostMapping(value = "/JobAdd" ,  consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<Job_Application> uploadImage(@RequestParam("cv") MultipartFile cvFile,@RequestPart("lettreMotivation") MultipartFile lettreMotivationFile) {
        try {
            Job_Application savedImageData = iJobApplication.save(cvFile.getBytes(),lettreMotivationFile.getBytes(),cvFile.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedImageData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /*@GetMapping(value = "/application/{id}/pdf",produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource>uploadCVAndLetter(@PathVariable Long id) throws Exception {
        Job_Application application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job application not found with id " + id));

        // Check that both the CV and the motivation letter exist
        if (application.getCv() == null || application.getLettreMotivation() == null) {
            throw new Exception("Both the CV and the motivation letter must be uploaded before generating the PDF.");
        }

        // Generate PDF document from the CV and motivation letter bytes
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        document.add(new Paragraph("CV"));
        document.add(new Paragraph(new String(application.getCv())));
        document.add(new Paragraph("Motivation Letter"));
        document.add(new Paragraph(new String(application.getLettreMotivation())));
        document.close();

        // Return PDF as byte array with appropriate headers
        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=application.pdf");
        ResponseEntity<Resource> response = new ResponseEntity<>(resource, headers, HttpStatus.OK);
        return response;
    }*/
    @GetMapping(value = "/application/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> generatePdfFromApplication(@PathVariable Long id) throws Exception {
        Job_Application application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job application not found with id " + id));

        if (application.getCv() == null || application.getLettreMotivation() == null) {
            throw new Exception("Both the CV and the motivation letter must be uploaded before generating the PDF.");
        }

        // Get the text content from the CV and motivation letter bytes
        String cvText = new String(application.getCv());
        String motivationText = new String(application.getLettreMotivation());

        // Create a PDF document
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        // Add content to the PDF
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
        Chunk cvTitle = new Chunk("CV", font);
        Chunk cvContent = new Chunk(cvText, font);
        Chunk motivationTitle = new Chunk("Motivation Letter", font);
        Chunk motivationContent = new Chunk(motivationText, font);

        document.add(new Paragraph(cvTitle));
        document.add(new Paragraph(cvContent));
        document.add(new Paragraph(motivationTitle));
        document.add(new Paragraph(motivationContent));
        document.close();

        // Return PDF as byte array with appropriate headers
        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=application.pdf");
        ResponseEntity<Resource> response = new ResponseEntity<>(resource, headers, HttpStatus.OK);
        return response;
    }













    @GetMapping("getAllJobApplications/")
    public List<Job_Application> GetAllJobApplications(){
        return iJobApplication.GetAllJobApplications();

    }

    /*@PutMapping("updateJobApplication/{id}")
    public Job_Application UpdateJobApplication(@PathVariable("id") Long Id_Job_Application , @RequestBody Job_Application job_application){
        return iJobApplication.UpdateJobApplication(Id_Job_Application,job_application);

    }*/


    @DeleteMapping("deleteJobApplication/{id}")
    public String DeleteJobApplication (@PathVariable("id") Long Id_Job_Application){
        return iJobApplication.DeleteJobApplication(Id_Job_Application);

    }
}
