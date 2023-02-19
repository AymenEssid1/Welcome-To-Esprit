package tn.esprit.springfever.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.dto.PostDTO;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.PostMedia;
import tn.esprit.springfever.services.interfaces.IPostMediaService;
import tn.esprit.springfever.services.interfaces.IPostService;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("post")
@Api(tags = "Posts Module")
public class PostController {
    @Autowired
    private IPostService service;
    @Autowired
    private IPostMediaService mediaService;

    @ApiOperation(value = "This method is used to add a post ")
    @PostMapping(value = "/", consumes = "multipart/form-data", produces = "application/json")
    @ResponseBody
    public Post addPost(@RequestParam String post, @RequestParam(name = "file", required = false) List<MultipartFile> images) throws IOException {
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
        return newP;
    }

    @ApiOperation(value = "This method is used to delete a post ")
    @DeleteMapping(value = "/")
    @ResponseBody
    public String deletePost(Long id) {
        Post p = service.getSinglePost(id);
        if (p != null) {
            if (p.getMedia() != null){
                for (PostMedia m: p.getMedia() ){
                    mediaService.delete(m.getId());
                }
            }
            service.deletePost(p.getId());
            return "Post Deleted!";
        }
        return "Post not found!";

    }


    @ApiOperation(value = "This method is used to delete a post ")
    @PutMapping(value = "/")
    @ResponseBody
    public String updatePost(Long id) {
        Post p = service.getSinglePost(id);
        if (p != null) {
            if (p.getMedia() != null){
                for (PostMedia m: p.getMedia() ){
                    mediaService.delete(m.getId());
                }
            }
            service.deletePost(p.getId());
            return "Post Deleted!";
        }
        return "Post not found!";

    }


}
