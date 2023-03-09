package tn.esprit.springfever.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.dto.PostDTO;
import tn.esprit.springfever.entities.Media;
import tn.esprit.springfever.services.interfaces.IMediaService;
import tn.esprit.springfever.services.interfaces.IPostService;
import tn.esprit.springfever.services.interfaces.IReactionService;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("posts")
@Tag(name = "Posts Module")
@Service
@CrossOrigin
public class PostController {
    @Autowired
    private IPostService service;

    @Autowired
    private IReactionService reactionService;

    @Autowired
    private IMediaService mediaService;

    @PostMapping(value = "/", consumes = "multipart/form-data", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> addPost(@RequestParam String title, @RequestParam String content, @RequestParam(name = "file", required = false) List<MultipartFile> images, HttpServletRequest authentication) throws IOException {
        return service.addPost(title, content,  authentication, images);
    }

    @DeleteMapping(value = "/")
    @ResponseBody
    public ResponseEntity<?> deletePost(Long id, HttpServletRequest authentication) throws IOException {
        String response = service.deletePost(id, authentication);
        if (response.equals("Post deleted successfully")) {
            return ResponseEntity.ok().body(response);
        } else if (response.equals("Post not found!")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }


    @PutMapping(value = "/", consumes = "multipart/form-data", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> updatePost(Long id, @RequestParam(required = false) String title, @RequestParam(required = false) String content, @RequestParam(required = false) String topic, @RequestParam(name = "file", required = false) List<MultipartFile> images, HttpServletRequest authentication) throws IOException {
        return service.updatePost(id, title, content, topic, authentication, images);
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<PostDTO>> getAll(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) Long id, HttpServletRequest request) throws JsonProcessingException {
        if (id != null) {
            return ResponseEntity.ok().body(service.getByUserLazy(page, size, id, request));
        } else {
            return ResponseEntity.ok().body(service.getAllLazy(page, size, request));
        }
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<PostDTO>> search(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String keyword, HttpServletRequest request) throws IOException {
        return ResponseEntity.ok().body(service.searchPosts(keyword, page, size, request));

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDTO> getById(@PathVariable Long id) throws JsonProcessingException {
        return ResponseEntity.ok().body(service.getSinglePost(id, null));
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
    @PostMapping(value = "/like")
    @ResponseBody
    public ResponseEntity<?> like(Long reaction, Long post, HttpServletRequest request) throws JsonProcessingException {
        Object response = service.likePost(reaction, post, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(value = "/like")
    public ResponseEntity<String> dislike(Long post, HttpServletRequest request) throws JsonProcessingException {
        return ResponseEntity.ok().body(service.deleteReaction(post, request));
    }

    @PutMapping(value = "/like")
    @ResponseBody
    public ResponseEntity<?> change(Long post, Long reaction, HttpServletRequest request) {
        Object response = service.changeReaction(post, reaction, request);
        if (response.getClass() == String.class) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    }

    @GetMapping(value = "/rss")
    public ResponseEntity<?> rssFeed() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        return new ResponseEntity<>(service.rssFeed(), headers, HttpStatus.OK);
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

    @PostMapping("/report")
    public ResponseEntity<?> report(HttpServletRequest request, Long postId, String desc) throws JsonProcessingException {
        return service.reportPost(postId,request,desc);
    }
}
