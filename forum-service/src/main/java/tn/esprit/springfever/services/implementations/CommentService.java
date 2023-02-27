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
import tn.esprit.springfever.entities.*;
import tn.esprit.springfever.repositories.CommentPagingRepository;
import tn.esprit.springfever.repositories.CommentRepository;
import tn.esprit.springfever.repositories.ReactionRepository;
import tn.esprit.springfever.services.interfaces.ICommentService;
import tn.esprit.springfever.services.interfaces.ILikesService;
import tn.esprit.springfever.services.interfaces.IMediaService;
import tn.esprit.springfever.services.interfaces.IPostService;
import tn.esprit.springfever.utils.MediaComparator;
import tn.esprit.springfever.utils.MultipartFileSizeComparator;

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
    private IMediaService mediaService;
    @Autowired
    private ProfanitiesService profanitiesService;
    @Autowired
    private UserService userService;
    @Autowired
    private ILikesService likesService;
    @Autowired
    private ReactionRepository reactionRepository;

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
                            Media savedImageData = mediaService.save(image);
                            newC.getMedia().add(savedImageData);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
            repo.save(newC);
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
                        List<Media> mediaList = p.getMedia();
                        if (mediaList != null && images != null) {
                            Collections.sort(images, new MultipartFileSizeComparator());
                            Collections.sort(mediaList, new MediaComparator());
                            for (Media m : new ArrayList<>(mediaList)) {
                                for (MultipartFile f : new ArrayList<>(images)) {
                                    if (m.getContent().length == f.getBytes().length) {
                                        images.remove(f);
                                        mediaList.remove(m);
                                        break;
                                    }
                                }
                            }
                            for (Media m : mediaList) {
                                mediaService.delete(m.getMediaId());
                            }
                        }
                        if (images != null) {
                            if (!images.isEmpty()) {
                                for (MultipartFile image : images) {
                                    if (!image.isEmpty()) {
                                        try {
                                            Media savedImageData = mediaService.save(image);
                                            p.getMedia().add(savedImageData);
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
                        for (Media m : p.getMedia()) {
                            mediaService.delete(m.getMediaId());
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

    @Override
    public Object likeComment(Long reaction, Long comment, HttpServletRequest request) throws JsonProcessingException {
        if (request != null && request.getHeader(HttpHeaders.AUTHORIZATION) != null) {
            UserDTO user = userService.getUserDetailsFromToken(request.getHeader(HttpHeaders.AUTHORIZATION));
            Likes like = new Likes();
            like.setUser(user.getId());
            like.setType(reactionRepository.findById(reaction).orElse(null));
            if (!likesService.findByUser(user.getId()).contains(like)) {
                Comment p = repo.findById(comment).orElse(null);
                if (p != null) {
                    p.getLikes().add(like);
                    repo.save(p);
                    return likesService.addLike(like);
                } else {
                    return "The comment you're trying to react to is not found!";
                }
            } else {
                return "You already reacted to this comment!";
            }
        } else {
            return "You have to login to react to a comment";
        }
    }

    @Override
    public Object changeReaction(Long id, Long reaction, HttpServletRequest request) {
        Object ret = null;
        try {
            if (request != null && request.getHeader(HttpHeaders.AUTHORIZATION) != null)
                ret = likesService.updatePostLike(id, reaction, userService.getUserDetailsFromToken(request.getHeader(HttpHeaders.AUTHORIZATION)).getId());
        } catch (Exception e) {
            ret = "Error";
        }
        return ret;
    }

    @Override
    public String deleteReaction(Long id, HttpServletRequest request) throws JsonProcessingException {
        if (request == null || request.getHeader(HttpHeaders.AUTHORIZATION) == null) {
            return "You have to login";
        } else {
            return likesService.deletePostLike(id, userService.getUserDetailsFromToken(request.getHeader(HttpHeaders.AUTHORIZATION)).getId());
        }
    }

}
