package tn.esprit.springfever.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.dto.CommentDTO;
import tn.esprit.springfever.entities.Comment;
import tn.esprit.springfever.entities.Likes;
import tn.esprit.springfever.entities.Media;
import tn.esprit.springfever.services.interfaces.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/comments")
@Api(tags = "Comments Module")
@Tag(name = "Comments Module")
@CrossOrigin
public class CommentController {
    @Autowired
    private ICommentService service;

    @Autowired
    private IMediaService mediaService;

    @Autowired
    private IReactionService reactionService;

    @ApiOperation(value = "This method is used to add a post ")
    @PostMapping(value = "/", consumes = "multipart/form-data", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> addPost(@RequestParam String comment, @RequestParam(name = "file", required = false) List<MultipartFile> images, @RequestParam Long postId, HttpServletRequest request) throws IOException {
        return service.addComment(comment,images,postId,request);
    }

    @ApiOperation(value = "This method is used to delete a post ")
    @DeleteMapping(value = "/")
    @ResponseBody
    public ResponseEntity<String> deletePost(Long id, HttpServletRequest authentication) throws JsonProcessingException {
        String response = service.deleteComment(id, authentication);
        if (response.equals("Comment deleted successfully")) {
            return ResponseEntity.ok().body(response);
        } else if (response.equals("Comment not found!")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

    }

    @ApiOperation(value = "This method is used to update a post ")
    @PutMapping(value = "/", consumes = "multipart/form-data", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> updatePost(Long id, @RequestParam String comment, @RequestParam(name = "file", required = false) List<MultipartFile> images, HttpServletRequest request) throws IOException {
        return service.updateComment(id, comment, images, request);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CommentDTO> getById(@PathVariable Long id) throws JsonProcessingException {
        return ResponseEntity.ok().body(service.getSingleComment(id));
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

    /**
     * The Likes part is implemented here
     **/

    @ApiOperation(value = "This method is used to like a post ")
    @PostMapping(value = "/like")
    @ResponseBody
    public ResponseEntity<?> like(Long reaction, Long post, HttpServletRequest request) throws JsonProcessingException {
        Object response = service.likeComment(reaction,post,request);
        return ResponseEntity.ok().body(response);
    }

    @ApiOperation(value = "This method is used to unlike a post")
    @DeleteMapping(value = "/like")
    public ResponseEntity<String> dislike(Long comment, HttpServletRequest request) throws JsonProcessingException {
        return ResponseEntity.ok().body(service.deleteReaction(comment, request));
    }

    @PutMapping(value = "/like")
    @ResponseBody
    public ResponseEntity<?> change(Long comment,Long reaction, HttpServletRequest request) {
        Object response = service.changeReaction(comment,reaction,request);
        if (response.getClass() == String.class){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }else{
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    }

    @GetMapping(value = "/download")
    public ResponseEntity<Resource> download(Long id) throws IOException {
        Media media = mediaService.findPath(id);
        Path path = Paths.get(media.getLocation());
        byte[] fileContent = Files.readAllBytes(path);

        // Build the response headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+media.getName());

        // Wrap the byte array in a resource object
        ByteArrayResource resource = new ByteArrayResource(fileContent);

        // Return the response entity
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }


}
