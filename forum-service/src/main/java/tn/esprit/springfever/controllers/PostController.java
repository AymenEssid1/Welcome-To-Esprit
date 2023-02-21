package tn.esprit.springfever.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.dto.PostDTO;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.PostLike;
import tn.esprit.springfever.entities.PostMedia;
import tn.esprit.springfever.services.interfaces.IPostLikeService;
import tn.esprit.springfever.services.interfaces.IPostMediaService;
import tn.esprit.springfever.services.interfaces.IPostService;
import tn.esprit.springfever.services.interfaces.IReactionService;
import tn.esprit.springfever.utils.MultipartFileSizeComparator;
import tn.esprit.springfever.utils.PostMediaComparator;

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
            if (p.getMedia() != null) {
                for (PostMedia m : p.getMedia()) {
                    mediaService.delete(m.getId());
                }
            }
            service.deletePost(p.getId());
            return ResponseEntity.ok().body("Post deleted!");
        }
        return ResponseEntity.notFound().build();

    }

    @ApiOperation(value = "This method is used to update a post ")
    @PutMapping(value = "/", consumes = "multipart/form-data", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> updatePost(Long id, @RequestParam String post, @RequestParam(name = "file", required = false) List<MultipartFile> images) throws IOException {
        Post p = service.getSinglePost(id);
        ObjectMapper objectMapper = new ObjectMapper();
        PostDTO postDTO = objectMapper.readValue(post, PostDTO.class);
        if (p != null) {
            List<PostMedia> mediaList = p.getMedia();
            if (mediaList != null && images != null) {
                Collections.sort(images, new MultipartFileSizeComparator());
                Collections.sort(mediaList, new PostMediaComparator());
                for (PostMedia m : new ArrayList<>(mediaList)) {
                    for (MultipartFile f : new ArrayList<>(images)) {
                        if (m.getContent().length == f.getBytes().length) {
                            images.remove(f);
                            mediaList.remove(m);
                            break;
                        }
                }

            }
            for (PostMedia m : mediaList) {
                mediaService.delete(m.getId());
            }
        }
        if (images != null) {
            if (!images.isEmpty()) {
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        try {
                            PostMedia savedImageData = mediaService.save(image, p);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }

        }
        p.setTitle(postDTO.getTitle());
        p.setContent(postDTO.getContent());
        service.updatePost(p.getId(), p);
        return ResponseEntity.ok().body("Post updated!");
    }
        return ResponseEntity.notFound().build();

}
    @GetMapping(value = "/")
    public ResponseEntity<List<Post>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) Long id) {
        if (id != null) {
            return ResponseEntity.ok().body(service.getByUserLazy(page, size, id));
        } else {
            return ResponseEntity.ok().body(service.getAllLazy(page, size));
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Post> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.getSinglePost(id));
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
    public ResponseEntity<PostLike> like(@RequestBody int user, @RequestBody Long postId, @RequestBody Long reaction) {
        PostLike pl = new PostLike();
        pl.setType(reactionService.getById(reaction));
        pl.setPost(service.getSinglePost(postId));
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


}
