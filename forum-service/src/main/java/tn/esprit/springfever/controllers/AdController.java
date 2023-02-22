package tn.esprit.springfever.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.dto.AdDTO;
import tn.esprit.springfever.entities.Ad;
import tn.esprit.springfever.entities.AdMedia;
import tn.esprit.springfever.utils.AdMediaComparator;
import tn.esprit.springfever.utils.MultipartFileSizeComparator;
import tn.esprit.springfever.services.interfaces.IAdMediaService;
import tn.esprit.springfever.services.interfaces.IAdService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("advertisement")
@Tag(name = "Advertisement Module")
@Api(tags = "Advertisement Module")
@Service
public class AdController {
    @Autowired
    private IAdService service;
    @Autowired
    private IAdMediaService mediaService;


    @ApiOperation(value = "This method is used to add an advertisement ")
    @PostMapping(value = "/", consumes = "multipart/form-data", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Ad> addAd(@RequestParam String ad, @RequestParam(name = "file", required = false) List<MultipartFile> images) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        AdDTO adDTO = objectMapper.readValue(ad, AdDTO.class);
        Ad c = new Ad();
        c.setChannel(adDTO.getChannel());
        c.setCost(adDTO.getCost());
        c.setEndDate(adDTO.getEndDate());
        c.setStartDate(adDTO.getStartDate());
        c.setInitialTargetNumberOfViews(adDTO.getInitialTargetNumberOfViews());
        c.setName(adDTO.getName());
        c.setTypeOfAudience(adDTO.getTypeOfAudience());
        Ad newAd = service.addAd(c);
        if (images != null) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    try {
                        AdMedia savedImageData = mediaService.save(image, newAd);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newAd);
    }

    @ApiOperation(value = "This method is used to delete a post ")
    @DeleteMapping(value = "/")
    @ResponseBody
    public ResponseEntity<String> deletePost(Long id) {
        Ad p = service.getSingleAd(id);
        if (p != null) {
            if (p.getMedia() != null) {
                for (AdMedia m : p.getMedia()) {
                    mediaService.delete(m.getId());
                }
            }
            service.deleteAd(p.getId());
            return ResponseEntity.ok().body("Post deleted!");
        }
        return ResponseEntity.notFound().build();

    }

    @ApiOperation(value = "This method is used to update a post ")
    @PutMapping(value = "/", consumes = "multipart/form-data", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> updatePost(Long id, @RequestParam String ad, @RequestParam(name = "file", required = false) List<MultipartFile> images) throws IOException {
        Ad p = service.getSingleAd(id);
        ObjectMapper objectMapper = new ObjectMapper();
        AdDTO adDTO = objectMapper.readValue(ad, AdDTO.class);
        if (p != null) {
            List<AdMedia> mediaList = p.getMedia();
            if (mediaList != null && images != null) {
                Collections.sort(images, new MultipartFileSizeComparator());
                Collections.sort(mediaList, new AdMediaComparator());
                for (AdMedia m : new ArrayList<>(mediaList)) {
                    for (MultipartFile f : new ArrayList<>(images)) {
                        if (m.getContent().length == f.getBytes().length) {
                            images.remove(f);
                            mediaList.remove(m);
                            break;
                        }
                    }

                }
                for (AdMedia m : mediaList) {
                    mediaService.delete(m.getId());
                }
            }
            if (images != null) {
                if (!images.isEmpty()) {
                    for (MultipartFile image : images) {
                        if (!image.isEmpty()) {
                            try {
                                AdMedia savedImageData = mediaService.save(image, p);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                }

            }
            p.setChannel(adDTO.getChannel());
            p.setCost(adDTO.getCost());
            p.setEndDate(adDTO.getEndDate());
            p.setStartDate(adDTO.getStartDate());
            p.setInitialTargetNumberOfViews(adDTO.getInitialTargetNumberOfViews());
            p.setName(adDTO.getName());
            p.setTypeOfAudience(adDTO.getTypeOfAudience());
            service.updateAd(p.getId(), p);
            return ResponseEntity.ok().body("Post updated!");
        }
        return ResponseEntity.notFound().build();

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Ad> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.getSingleAd(id));
    }

    @GetMapping(value = "/media/{imageId}")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "*/*"))
    @ApiResponse(responseCode = "404", description = "Image not found")
    @Schema(format = "binary")
    public ResponseEntity<FileSystemResource> getMediaById(@PathVariable Long imageId) {
        try {
            FileSystemResource fileSystemResource = mediaService.find(imageId);
            MediaType mediaType = MediaTypeFactory.getMediaType(fileSystemResource).orElse(MediaType.APPLICATION_OCTET_STREAM);
            return ResponseEntity.ok().contentType(mediaType).body(fileSystemResource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<Ad>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) Long id) {
        return ResponseEntity.ok().body(service.getAllLazy(page, size));
    }

}
