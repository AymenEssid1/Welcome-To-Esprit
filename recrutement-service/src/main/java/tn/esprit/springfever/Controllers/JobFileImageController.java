package tn.esprit.springfever.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.Services.Interfaces.IJobFileImage;
import tn.esprit.springfever.entities.Image_JobOffer;

@RestController
public class JobFileImageController {

    @Autowired
    IJobFileImage iJobFileImage;


    @PostMapping(value = "/image" ,  consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<Image_JobOffer> uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            Image_JobOffer savedImageData = iJobFileImage.save(image.getBytes(), image.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedImageData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/image/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<FileSystemResource> downloadImage(@PathVariable Long imageId) {
        try {
            FileSystemResource fileSystemResource = iJobFileImage.find(imageId);
            return  ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(fileSystemResource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


}
