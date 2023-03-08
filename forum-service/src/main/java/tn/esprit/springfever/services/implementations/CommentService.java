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
import tn.esprit.springfever.dto.CommentDTO;
import tn.esprit.springfever.dto.LikesDTO;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.*;
import tn.esprit.springfever.repositories.CommentPagingRepository;
import tn.esprit.springfever.repositories.CommentRepository;
import tn.esprit.springfever.repositories.PostRepository;
import tn.esprit.springfever.repositories.ReactionRepository;
import tn.esprit.springfever.services.interfaces.*;
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
    private IMediaService mediaService;
    @Autowired
    private ProfanitiesService profanitiesService;
    @Autowired
    private UserService userService;
    @Autowired
    private IReportService reportService;
    @Autowired
    private ILikesService likesService;
    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public ResponseEntity<?> addComment(String comment, List<MultipartFile> images, Long postId, HttpServletRequest authentication) throws JsonProcessingException {
        if (
                profanitiesService.containsBannedWords(comment)
        ) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(
                            "{\"Message\": \"Please check your post! We do not allow bad language\"}"
                    );
        } else if (
                authentication == null ||
                        authentication.getHeader(HttpHeaders.AUTHORIZATION) == null
        ) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("{\"Message\": \"Login or sign up to post!\"}");
        } else if(postRepository.findById(postId).orElse(null)== null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("{\"Message\": \"The post you're trying to comment on does not exist!!\"}");
        }else {
            Comment p = new Comment();
            p.setContent(comment);
            p.setCreatedAt(LocalDateTime.now());
            p.setPost(postRepository.findById(postId).orElse(null));
            p.setUpdatedAt(LocalDateTime.now());
            p.setUser(
                    Long.valueOf(
                            userService
                                    .getUserDetailsFromToken(
                                            authentication.getHeader(HttpHeaders.AUTHORIZATION)
                                    )
                                    .getId()
                    )
            );
            if (images != null) {
                List<Media> listM = new ArrayList<>();
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        try {
                            Media savedImageData = mediaService.save(image);
                            listM.add(savedImageData);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
                p.setMedia(listM);
            }
            repo.save(p);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToLikesDTO(p));
        }
    }

    @Override
    @CachePut("comment")
    public ResponseEntity<?> updateComment(Long id, String comment, List<MultipartFile> images, HttpServletRequest authentication) throws IOException {
        if (
                profanitiesService.containsBannedWords(comment)
        ) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(
                            "{\"Message\": \"Please check your post! We do not allow bad language\"}"
                    );
        } else if (
                authentication == null ||
                        authentication.getHeader(HttpHeaders.AUTHORIZATION) == null
        ) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("{\"Message\": \"Login or sign up to post!\"}");
        } else {
            Comment p = repo.findById(Long.valueOf(id)).orElse(null);
            UserDTO user = null;
            if (
                    authentication != null &&
                            authentication.getHeader(HttpHeaders.AUTHORIZATION) != null
            ) {
                user =
                        userService.getUserDetailsFromToken(
                                authentication.getHeader(HttpHeaders.AUTHORIZATION)
                        );
                if (p != null) {
                    if (
                            user.getId() == p.getUser() ||
                                    user.getRoles().contains("FORUM_ADMIN") ||
                                    user.getRoles().contains("SUPER_ADMIN")
                    ) {
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
                        return ResponseEntity.ok().body(convertToLikesDTO(p));
                    } else {
                        return ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .body("{\"Message\": \"That's not yours to edit!\"}");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
            } else {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body("{\"Message\": \"Login or sign up to post!\"}");
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
    public CommentDTO getSingleComment(Long id) throws JsonProcessingException {
        Comment comment =repo.findById(id).orElse(null);
        if (comment!=null){
            return convertToLikesDTO(comment);
        }
        else{
            return null;
        }
    }

    @Override
    public Object likeComment(Long reaction, Long comment, HttpServletRequest request) throws JsonProcessingException {
        if (request != null && request.getHeader(HttpHeaders.AUTHORIZATION) != null) {
            UserDTO user = userService.getUserDetailsFromToken(request.getHeader(HttpHeaders.AUTHORIZATION));
            Likes like = new Likes();
            like.setUser(user.getId());
            like.setCreatedAt(LocalDateTime.now());
            like.setUpdatedAt(LocalDateTime.now());
            like.setType(reactionRepository.findById(reaction).orElse(null));
            boolean liked = false;
            Comment p = repo.findById(comment).orElse(null);
            for (Likes pl : p.getLikes()) {
                if (pl.getUser() == user.getId()) {
                    liked = true;
                }
                log.warn("already liked!");
            }
            if (!liked) {

                if (p != null) {
                    p.getLikes().add(like);
                    repo.save(p);
                    return "Liked!";
                } else {
                    return "The post you're trying to react to is not found!";
                }
            } else {
                return "You already reacted to this post!";
            }
        } else {
            return "You have to login to react to a post";
        }
    }

    @Override
    public Object changeReaction(Long id, Long reaction, HttpServletRequest request) {
        Object ret = null;
        try {
            if (request != null && request.getHeader(HttpHeaders.AUTHORIZATION) != null)
                ret = likesService.updateCommentLike(repo.findById(id).orElse(null), reaction, userService.getUserDetailsFromToken(request.getHeader(HttpHeaders.AUTHORIZATION)).getId());
        } catch (Exception e) {
            ret = "Error";
        }
        return ret;
    }

    @Override
    public String deleteReaction(Long commentId, HttpServletRequest request) throws JsonProcessingException {
        if (request == null || request.getHeader(HttpHeaders.AUTHORIZATION) == null) {
            return "You have to login";
        } else {
            return likesService.deleteCommentLike(repo.findById(commentId).orElse(null), userService.getUserDetailsFromToken(request.getHeader(HttpHeaders.AUTHORIZATION)).getId());
        }
    }

    @Override
    public List<CommentDTO> convertToLikesDTOS(List<Comment> comments) throws JsonProcessingException {
        List<CommentDTO> commentDTOS = new ArrayList<>();
        List<Long> list = new ArrayList<>();
        for (Comment comment : comments) {
            list.add(comment.getUser());
        }
        List<UserDTO> users = userService.getUserDetailsFromIds(list);
        for (Comment comment : comments){
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setId(comment.getId());
            if (comment.getLikes() != null){
                commentDTO.setLikes(likesService.convertToLikesDTOS(comment.getLikes()));
            }
            if(comment.getMedia()!=null){
                commentDTO.setMedia(comment.getMedia());
            }
            commentDTO.setContent(comment.getContent());
            commentDTO.setUser(users.get(comments.indexOf(comment)));
            commentDTOS.add(commentDTO);
        }
        return commentDTOS;
    }

    @Override
    public CommentDTO convertToLikesDTO(Comment comment) throws JsonProcessingException {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        if (comment.getLikes() != null){
            commentDTO.setLikes(likesService.convertToLikesDTOS(comment.getLikes()));
        }
        if(comment.getMedia()!=null){
            commentDTO.setMedia(comment.getMedia());
        }
        commentDTO.setContent(comment.getContent());
        commentDTO.setUser(userService.getUserDetailsFromId(comment.getUser()));
        return commentDTO;
    }
    @Override
    public ResponseEntity<?> reportComment(Long id, HttpServletRequest request, String desc) throws JsonProcessingException {
        if (request == null || request.getHeader(HttpHeaders.AUTHORIZATION) == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            Long userId = userService.getUserDetailsFromToken(request.getHeader(HttpHeaders.AUTHORIZATION)).getId();
            Comment comment = repo.findById(id).orElse(null);
            if(comment==null){
                return ResponseEntity.notFound().build();
            }else{
                Report report = reportService.reportComment(comment,userId,desc);
                if (report == null){
                    return ResponseEntity.notFound().build();
                }
                if (comment.getReports().size()>5){
                    repo.delete(comment);
                }
                return ResponseEntity.ok().body(report);
            }
        }
    }


}
