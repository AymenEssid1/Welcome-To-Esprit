package tn.esprit.springfever.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.dto.PostDTO;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.PostLike;
import tn.esprit.springfever.entities.PostMedia;
import tn.esprit.springfever.services.interfaces.IPostLikeService;
import tn.esprit.springfever.services.interfaces.IPostMediaService;
import tn.esprit.springfever.services.interfaces.IPostService;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("post")
@Tag(name = "Posts Module")
@Api(tags = "Posts Module")
public class PostController {
    @Autowired
    private IPostService service;

    @Autowired
    private IPostLikeService likeService;
    @Autowired
    private IPostMediaService mediaService;

    @ApiOperation(value = "This method is used to add a post ")
    @PostMapping(value = "/", consumes = "multipart/form-data", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Post> addPost(@RequestParam String post, @RequestParam(name = "file", required = false) List<MultipartFile> images) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PostDTO postDTO = objectMapper.readValue(post, PostDTO.class);
        Post p = new Post();
        p.setTitle(postDTO.getTitle());
        p.setContent(postDTO.getContent());
        Post newP = service.addPost(p);
        if (images != null) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    try {
                        PostMedia savedImageData = mediaService.save(image, newP);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newP);
    }

    @ApiOperation(value = "This method is used to delete a post ")
    @DeleteMapping(value = "/")
    @ResponseBody
    public ResponseEntity<String> deletePost(Long id) {
        Post p = service.getSinglePost(id);
        if (p != null) {
            if (p.getMedia() != null){
                for (PostMedia m: p.getMedia() ){
                    mediaService.delete(m.getId());
                }
            }
            service.deletePost(p.getId());
            return ResponseEntity.ok().body("Post deleted!");
        }
        return ResponseEntity.notFound().build();

    }


    @ApiOperation(value = "This method is used to delete a post ")
    @PutMapping(value = "/")
    @ResponseBody
    public ResponseEntity<String> updatePost(Long id) {
        Post p = service.getSinglePost(id);
        if (p != null) {
            if (p.getMedia() != null){
                for (PostMedia m: p.getMedia() ){
                    mediaService.delete(m.getId());
                }
            }
            service.deletePost(p.getId());
            return ResponseEntity.ok().body("Post updated!");
        }
        return ResponseEntity.notFound().build();

    }

    @GetMapping(value = "/")
    public ResponseEntity<List<Post>> getAll(){
        return ResponseEntity.ok().body(service.getAllPosts());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Post> getById(@PathVariable Long id){
        return ResponseEntity.ok().body(service.getSinglePost(id));
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<List<Post>> getByUser(@PathVariable Long id){
        return ResponseEntity.ok().body(service.getByUser(id));
    }


    @GetMapping(value = "/media/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<FileSystemResource> getMediaById(@PathVariable Long imageId) {
        try {
            FileSystemResource fileSystemResource = mediaService.find(imageId);
            return  ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(fileSystemResource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<FileSystemResource> getPostById(@PathVariable Long imageId) {
        try {
            FileSystemResource fileSystemResource = mediaService.find(imageId);
            return  ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(fileSystemResource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    @ApiOperation(value = "This method is used to like a post ")
    @PostMapping(value = "/like")
    @ResponseBody
    public ResponseEntity<PostLike> like(PostLike pl){
        return ResponseEntity.status(HttpStatus.CREATED).body(likeService.addPostLike(pl));
    }
    @ApiOperation(value = "This method is used to unlike a post")
    @DeleteMapping(value = "/like")
    public ResponseEntity<String> unlike(Long id){
        return ResponseEntity.ok().body(likeService.deletePostLike(id));
    }
    @Description(value = "This method is used to change the type of likes")
    @PutMapping(value = "/like")
    @ResponseBody
    public ResponseEntity<PostLike> change(Long id, PostLike pl){
        return ResponseEntity.ok().body(likeService.updatePostLike(id, pl));
    }


}
