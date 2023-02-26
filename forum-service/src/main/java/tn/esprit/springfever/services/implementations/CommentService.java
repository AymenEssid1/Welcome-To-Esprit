package tn.esprit.springfever.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.Comment;
import tn.esprit.springfever.entities.CommentMedia;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.PostMedia;
import tn.esprit.springfever.repositories.CommentPagingRepository;
import tn.esprit.springfever.repositories.CommentRepository;
import tn.esprit.springfever.services.interfaces.ICommentMediaService;
import tn.esprit.springfever.services.interfaces.ICommentService;
import tn.esprit.springfever.services.interfaces.IPostService;
import tn.esprit.springfever.utils.CommentMediaComparator;
import tn.esprit.springfever.utils.MultipartFileSizeComparator;
import tn.esprit.springfever.utils.PostMediaComparator;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class CommentService implements ICommentService {
    @Autowired
    private CommentRepository repo;
    @Autowired
    private IPostService postService;
    @Autowired
    private ICommentMediaService mediaService;
    @Autowired
    private ProfanitiesService profanitiesService;
    @Autowired
    private UserService userService;

    @Autowired
    private CommentPagingRepository pagingRepository;

    @Override
    public ResponseEntity<?> addComment(String comment, List<MultipartFile> images, Long postId, HttpServletRequest request) {
        if (profanitiesService.containsBannedWords(comment)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"Message\": \"Please check your post! We do not allow bad language\"}");
        } else if (request == null || request.getHeader(HttpHeaders.AUTHORIZATION) == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"Message\": \"Login or sign up to post!\"}");
        } else {
            Comment c = new Comment();
            c.setContent(comment);
            c.setPost(postService.getSinglePost(postId, null));
            c.setCreatedAt(LocalDateTime.now());
            c.setUpdatedAt(LocalDateTime.now());
            Comment newC = repo.save(c);
            if (images != null) {
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        try {
                            CommentMedia savedImageData = mediaService.save(image, newC);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(newC);
        }
    }

    @Override
    @CachePut("comment")
    public ResponseEntity<?> updateComment(Long id, String comment, List<MultipartFile> images, HttpServletRequest authentication) throws IOException {
        if (profanitiesService.containsBannedWords(comment)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"Message\": \"Please check your post! We do not allow bad language\"}");
        } else if (authentication == null || authentication.getHeader(HttpHeaders.AUTHORIZATION) == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"Message\": \"Login or sign up to post!\"}");
        } else {
            Comment p = repo.findById(Long.valueOf(id)).orElse(null);
            UserDTO user = null;
            if (authentication != null && authentication.getHeader(HttpHeaders.AUTHORIZATION) != null) {
                user = userService.getUserDetailsFromToken(authentication.getHeader(HttpHeaders.AUTHORIZATION));
                if (p != null) {
                    if (user.getId() == p.getUser() || user.getRoles().contains("FORUM_ADMIN") || user.getRoles().contains("SUPER_ADMIN")) {
                        p.setUpdatedAt(LocalDateTime.now());
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
                        if (comment != null) {
                            p.setContent(comment);
                        }
                        repo.save(p);
                        return ResponseEntity.ok().body(p);
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"Message\": \"That's not yours to edit!\"}");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"Message\": \"Login or sign up to post!\"}");
            }
        }
    }


    @Override
    @CacheEvict("comment")
    public String deleteComment(Long comment, HttpServletRequest authentication) throws JsonProcessingException {
        Comment p = repo.findById(Long.valueOf(comment)).orElse(null);
        if (authentication != null) {
            UserDTO user = userService.getUserDetailsFromToken(authentication.getHeader(HttpHeaders.AUTHORIZATION));
            if (p != null) {
                if (user.getId() == p.getUser() || user.getRoles().contains("FORUM_ADMIN") || user.getRoles().contains("SUPER_ADMIN")) {
                    if (p.getMedia() != null) {
                        for (CommentMedia m : p.getMedia()) {
                            mediaService.delete(m.getId());
                        }
                    }
                    repo.delete(p);
                    return "Comment deleted successfully";
                } else {
                    return "You can't delete this post!";
                }
            } else {
                return "Comment not found!";
            }
        } else {
            return "Log in to delete th post!";
        }
    }

    @Override
    @Cacheable("comment")
    public Comment getSingleComment(Long id) {
        return repo.findById(id).orElse(null);
    }

}
