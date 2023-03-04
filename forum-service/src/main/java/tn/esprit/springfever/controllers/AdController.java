package tn.esprit.springfever.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
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
import tn.esprit.springfever.enums.Channel;
import tn.esprit.springfever.utils.AdMediaComparator;
import tn.esprit.springfever.utils.MultipartFileSizeComparator;
import tn.esprit.springfever.services.interfaces.IAdMediaService;
import tn.esprit.springfever.services.interfaces.IAdService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
    public ResponseEntity<?> addAd(@RequestParam Channel channel, @RequestParam float cost, @RequestParam String startDate,
                                   @RequestParam String endDate, @RequestParam int initialTarget, @RequestParam String name,
                                   @RequestParam Long targetPopulation, @RequestParam(name = "file", required = false) List<MultipartFile> images,
                                   HttpServletRequest authentication) throws IOException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date start = format.parse(startDate);
        Date end = format.parse(endDate);
        return service.addAd(channel, cost, start, end, initialTarget, name, targetPopulation, images, authentication);
    }

    @ApiOperation(value = "This method is used to delete a post ")
    @DeleteMapping(value = "/")
    @ResponseBody
    public ResponseEntity<String> deletePost(Long id, HttpServletRequest authentication) throws JsonProcessingException {
        return service.deleteAd(id, authentication);

    }

    @ApiOperation(value = "This method is used to update a post ")
    @PutMapping(value = "/", consumes = "multipart/form-data", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> updatePost(Long id,@RequestParam Channel channel, @RequestParam float cost, @RequestParam String startDate,
                                             @RequestParam String endDate, @RequestParam int initialTarget, @RequestParam String name,
                                             @RequestParam Long targetPopulation, @RequestParam(name = "file", required = false) List<MultipartFile> images,
                                             HttpServletRequest authentication) throws IOException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date start = format.parse(startDate);
        Date end = format.parse(endDate);
        return service.updateAd(id, channel, cost, start, end, initialTarget, name, targetPopulation, images, authentication);

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Ad> getById(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok().body(service.getSingleAd(id, request));
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
    public ResponseEntity<List<Ad>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, HttpServletRequest request) {
        return ResponseEntity.ok().body(service.getAllLazy(page, size, request));
    }

}
