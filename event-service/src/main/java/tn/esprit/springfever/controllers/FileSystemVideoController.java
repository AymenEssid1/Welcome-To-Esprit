package tn.esprit.springfever.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.Services.Implementation.FileLocationService;
import tn.esprit.springfever.entities.Video;


@RestController
@RequestMapping("file-system-video")
public class FileSystemVideoController {

    @Autowired
    FileLocationService fileLocationService;

    @PostMapping(value = "saveVideo" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Video uploadVideo(@RequestParam MultipartFile video) throws Exception{
        return fileLocationService.saveVideo(video.getBytes(), video.getOriginalFilename());
    }




    @ApiOperation(value = "Download a video file by ID", produces = "video/mp4")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of video file", response = FileSystemResource.class),
            @ApiResponse(code = 404, message = "Video file not found")
    })
    @GetMapping(value = "/video/{videoId}", produces =  "video/mp4" )
    ResponseEntity<FileSystemResource> downloadVideo(@PathVariable Long videoId) throws Exception {
        //return fileLocationService.findVideo(videoId);
        try {
            FileSystemResource fileSystemResource =fileLocationService.findVideo(videoId);
            MediaType mediaType = MediaTypeFactory.getMediaType(fileSystemResource).orElse(MediaType.APPLICATION_OCTET_STREAM);
            return ResponseEntity.ok().contentType(mediaType).body( fileLocationService.findVideo(videoId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
