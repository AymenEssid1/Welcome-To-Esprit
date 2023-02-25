package tn.esprit.springfever.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.dto.RoleDTO;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.PostLike;
import tn.esprit.springfever.entities.PostMedia;
import tn.esprit.springfever.services.interfaces.IPostLikeService;
import tn.esprit.springfever.utils.MultipartFileSizeComparator;
import tn.esprit.springfever.utils.PostMediaComparator;
import tn.esprit.springfever.services.interfaces.IPostMediaService;
import tn.esprit.springfever.services.interfaces.IPostService;
import tn.esprit.springfever.services.interfaces.IReactionService;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("posts")
@Tag(name = "Posts Module")
@Api(tags = "Posts Module")
@Service
public class PostController {
    @Autowired
    private IPostService service;
    @Autowired
    private IPostLikeService likeService;
    @Autowired
    private IPostMediaService mediaService;

    @Autowired
    private IReactionService reactionService;

    @ApiOperation(value = "This method is used to add a post ")
    @PostMapping(value = "/", consumes = "multipart/form-data", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> addPost(@RequestParam String title, @RequestParam String content, @RequestParam String topic, @RequestParam(name = "file", required = false) List<MultipartFile> images, HttpServletRequest authentication) throws IOException {

        if (authentication.getHeader(HttpHeaders.AUTHORIZATION) != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.addPost(title, content, topic, authentication, images));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"Message\": \"Login or sign up to post!\"}");
        }

    }

    @ApiOperation(value = "This method is used to delete a post ")
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


    @ApiOperation(value = "This method is used to update a post ")
    @PutMapping(value = "/", consumes = "multipart/form-data", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> updatePost(Long id, @RequestParam(required = false) String title, @RequestParam(required = false) String content, @RequestParam(required = false) String topic, @RequestParam(name = "file", required = false) List<MultipartFile> images, HttpServletRequest authentication) throws IOException {
        Post p = service.updatePost(id, title, content, topic, authentication, images);
        if (p == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            return ResponseEntity.ok().body(p);
        }
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<Post>> getAll(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) Long id, HttpServletRequest request) {
        if (id != null) {
            return ResponseEntity.ok().body(service.getByUserLazy(page, size, id, request));
        } else {
            return ResponseEntity.ok().body(service.getAllLazy(page, size, request));
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Post> getById(@PathVariable Long id) {
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
    @ApiOperation(value = "This method is used to like a post ")
    @PostMapping(value = "/like")
    @ResponseBody
    public ResponseEntity<PostLike> like(@RequestBody int user, @RequestBody Long postId,
                                         @RequestBody Long reaction) {
        PostLike pl = new PostLike();
        pl.setType(reactionService.getById(reaction));
        pl.setPost(service.getSinglePost(postId, null));
        return ResponseEntity.status(HttpStatus.CREATED).body(likeService.addPostLike(pl));
    }

    @ApiOperation(value = "This method is used to unlike a post")
    @DeleteMapping(value = "/dislike")
    public ResponseEntity<String> dislike(Long id) {
        return ResponseEntity.ok().body(likeService.deletePostLike(id));
    }

    @PutMapping(value = "/like")
    @ResponseBody
    public ResponseEntity<PostLike> change(Long id, PostLike pl) {
        return ResponseEntity.ok().body(likeService.updatePostLike(id, pl));
    }

    @GetMapping(value = "/rss")
    public ResponseEntity<?> rssFeed() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        return new ResponseEntity<>(service.rssFeed(), headers, HttpStatus.OK);
    }


}
