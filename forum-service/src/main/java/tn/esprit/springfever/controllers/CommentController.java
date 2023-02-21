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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.dto.CommentDTO;
import tn.esprit.springfever.dto.PostDTO;
import tn.esprit.springfever.entities.*;
import tn.esprit.springfever.services.interfaces.*;
import tn.esprit.springfever.utils.CommentMediaComparator;
import tn.esprit.springfever.utils.MultipartFileSizeComparator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
    private IPostService postService;
    @Autowired
    private ICommentLikeService likeService;
    @Autowired
    private ICommentMediaService mediaService;

    @Autowired
    private IReactionService reactionService;

    @ApiOperation(value = "This method is used to add a post ")
    @PostMapping(value = "/", consumes = "multipart/form-data", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Comment> addPost(@RequestParam String comment, @RequestParam(name = "file", required = false) List<MultipartFile> images, Long postId) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CommentDTO commentDTO = objectMapper.readValue(comment, CommentDTO.class);
        Comment c = new Comment();
        c.setContent(commentDTO.getContent());
        c.setPost(postService.getSinglePost(postId));
        Comment newP = service.addComment(c);
        if (images != null) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    try {
                        CommentMedia savedImageData = mediaService.save(image, newP);
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
        Comment p = service.getSingleComment(id);
        if (p != null) {
            if (p.getMedia() != null) {
                for (CommentMedia m : p.getMedia()) {
                    mediaService.delete(m.getId());
                }
            }
            service.deleteComment(p.getId());
            return ResponseEntity.ok().body("Post deleted!");
        }
        return ResponseEntity.notFound().build();

    }

    @ApiOperation(value = "This method is used to update a post ")
    @PutMapping(value = "/", consumes = "multipart/form-data", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> updatePost(Long id, @RequestParam String post, @RequestParam(name = "file", required = false) List<MultipartFile> images) throws IOException {
        Comment p = service.getSingleComment(id);
        ObjectMapper objectMapper = new ObjectMapper();
        PostDTO postDTO = objectMapper.readValue(post, PostDTO.class);
        if (p != null) {
            List<CommentMedia> mediaList = p.getMedia();
            if (mediaList != null && images != null) {
                Collections.sort(images, new MultipartFileSizeComparator());
                Collections.sort(mediaList, new CommentMediaComparator());
                for (CommentMedia m : new ArrayList<>(mediaList)) {
                    for (MultipartFile f : new ArrayList<>(images)) {
                        if (m.getContent().length == f.getBytes().length) {
                            images.remove(f);
                            mediaList.remove(m);
                            break;
                        }
                    }

                }
                for (CommentMedia m : mediaList) {
                    mediaService.delete(m.getId());
                }
            }
            if (images != null) {
                if (!images.isEmpty()) {
                    for (MultipartFile image : images) {
                        if (!image.isEmpty()) {
                            try {
                                CommentMedia savedImageData = mediaService.save(image, p);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                }

            }
            p.setContent(postDTO.getContent());
            service.updateComment(p.getId(), p);
            return ResponseEntity.ok().body("Post updated!");
        }
        return ResponseEntity.notFound().build();

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Comment> getById(@PathVariable Long id) {
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
    public ResponseEntity<CommentLike> like( Long user,  Long postId,  Long reaction) {
        CommentLike pl = new CommentLike();
        pl.setType(reactionService.getById(reaction));
        pl.setComment(service.getSingleComment(postId));
        pl.setUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(likeService.addCommentLike(pl));
    }

    @ApiOperation(value = "This method is used to unlike a post")
    @DeleteMapping(value = "/dislike")
    public ResponseEntity<String> dislike(Long id) {
        return ResponseEntity.ok().body(likeService.deleteCommentLike(id));
    }

    @PutMapping(value = "/like")
    @ResponseBody
    public ResponseEntity<CommentLike> change(Long id, Long reaction) {
        return ResponseEntity.ok().body(likeService.updateCommentLike(id, reaction));
    }


}
