package tn.esprit.springfever.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.Services.Interface.IFileLocationService;
import tn.esprit.springfever.entities.Image;

import javax.ws.rs.Produces;

import static org.springframework.http.MediaType.APPLICATION_PDF;

@RestController
@RequestMapping("PDF")
public class testinController {


    @Autowired
    IFileLocationService iFileLocationService;

    @PostMapping(value = "/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Image> uploadImage(@RequestParam("pdf") MultipartFile pdf) {
        try {
            Image savedImageData = iFileLocationService.save(pdf);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedImageData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/pdf/{imageId}")
    @Produces(value = {MediaType.APPLICATION_PDF_VALUE})
    public ResponseEntity<FileSystemResource> downloadImage(@PathVariable Long imageId) {
        try {
            FileSystemResource fileSystemResource = iFileLocationService.find(imageId);
            return ResponseEntity.ok().contentType(APPLICATION_PDF).body(fileSystemResource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
