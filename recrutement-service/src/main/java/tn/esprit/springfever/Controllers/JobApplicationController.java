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
    public ResponseEntity<Job_Application> uploadImage(@RequestParam("cv") MultipartFile cvFile,@RequestPart("lettreMotivation") MultipartFile lettreMotivationFile) {
        try {
            Job_Application savedImageData = iJobApplication.save(cvFile.getBytes(),lettreMotivationFile.getBytes(),cvFile.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedImageData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/


    /*@PostMapping(value = "/JobAdd2" ,  consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<Job_Application> uploadImage(@RequestParam("cv") MultipartFile cvFile,@RequestParam("lettreMotivation") MultipartFile lettreMotivationFile ) {
        try {
            Job_Application savedImageData = iJobApplication.savef(cvFile.getBytes(), lettreMotivationFile.getBytes(),cvFile.getOriginalFilename(),lettreMotivationFile.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedImageData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/


    /*@GetMapping(value = "/jobApplication/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource[]> downloadImage(@PathVariable("id") Long imageId) {
        try {
            Ressource[] ressources = iJobApplication.find(imageId);
            return  ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(fileSystemResource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }*/
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
    /*@GetMapping(value = "/application/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
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
    }*/


    @GetMapping(value = "/application/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> generatePdfFromApplication(@PathVariable Long id) throws Exception {
        Job_Application application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job application not found with id " + id));

        if (application.getLocation_Cv() == null || application.getLocation_LettreMotivation() == null) {
            throw new Exception("Both the CV and the motivation letter must be uploaded before generating the PDF.");
        }

        // Get the resource for the CV and motivation letter
        Resource cvResource = new FileSystemResource(application.getLocation_Cv());
        Resource motivationResource = new FileSystemResource(application.getLocation_LettreMotivation());
        if (!cvResource.exists() || !motivationResource.exists()) {
            throw new Exception("Both the CV and the motivation letter must exist before generating the PDF.");
        }

        // Create a PDF document
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        // Add content to the PDF
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
        Chunk cvTitle = new Chunk("CV", font);
        Chunk motivationTitle = new Chunk("Motivation Letter", font);

        try {
            // Read the content of the CV and motivation letter as text and add it to the PDF
            String cvText = new String(Files.readAllBytes(cvResource.getFile().toPath()));
            String motivationText = new String(Files.readAllBytes(motivationResource.getFile().toPath()));
            Chunk cvContent = new Chunk(cvText, font);
            Chunk motivationContent = new Chunk(motivationText, font);

            document.add(new Paragraph(cvTitle));
            document.add(new Paragraph(cvContent));
            document.add(new Paragraph(motivationTitle));
            document.add(new Paragraph(motivationContent));
        } catch (IOException e) {
            // Handle file access or reading errors
            throw new RuntimeException("Error reading file content", e);
        } finally {
            document.close();

        }

        // Return PDF as byte array with appropriate headers
        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=application.pdf");
        ResponseEntity<Resource> response = new ResponseEntity<>(resource, headers, HttpStatus.OK);
        return response;
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


    /*@GetMapping(value = "/Jobbb/{id}")
    public ResponseEntity<Resource[]> downloadPDF(@PathVariable("id") Long Id) {
        try {
            System.out.println("mriguel") ;
            Resource[] resources = iJobApplication.find(Id);
            System.out.println("mriguel2") ;
            return ResponseEntity.ok()
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(resources);

        } catch (Exception e) {
            System.out.println("eror pdf") ;

            return ResponseEntity.notFound().build();
        }
    }*/


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
        String text = null;
        try {
            FileSystemResource fileSystemResource = iJobApplication.findLettreMotivation(Id);

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
}

