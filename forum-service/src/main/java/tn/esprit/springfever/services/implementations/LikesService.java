package tn.esprit.springfever.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.dto.LikesDTO;
import tn.esprit.springfever.dto.PostDTO;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.Comment;
import tn.esprit.springfever.entities.Likes;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.Reaction;
import tn.esprit.springfever.repositories.LikesRepository;
import tn.esprit.springfever.repositories.ReactionRepository;
import tn.esprit.springfever.services.interfaces.ILikesService;
import tn.esprit.springfever.services.interfaces.IUserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LikesService implements ILikesService {
    @Autowired
    private LikesRepository repo;
    @Autowired
    private ReactionRepository reactionRepository;
    @Autowired
    private IUserService userService;

    @Override
    public Likes updatePostLike(Post post, Long reaction, Long user) {
        if(post!=null){
            Likes p = repo.findByUserAndPost(user, post);
            Reaction r = reactionRepository.findById(reaction).orElse(null);
            if (p != null) {
                if (r != null && p.getUser() == user) {
                    p.setType(r);
                    p.setUpdatedAt(LocalDateTime.now());
                }
                return p;
            }
        }

        return null;
    }

    @Override
    public Likes updateCommentLike(Comment comment, Long reaction, Long user) {
        if(comment !=null){
            Likes p = repo.findByUserAndComment(user, comment);
            Reaction r = reactionRepository.findById(reaction).orElse(null);
            if (p != null) {
                if (r != null && p.getUser() == user) {
                    p.setType(r);
                    p.setUpdatedAt(LocalDateTime.now());
                }
                return p;
            }
        }
        return null;
    }

    @Override
    public Likes addLike(Likes like) {
        return repo.save(like);
    }

    @Override
    public String deletePostLike(Post post, Long user) {
        if(post!=null){
            Likes p = repo.findByUserAndPost(user, post);
            if (p != null && p.getUser() == user) {
                repo.delete(p);
                return "Post was successfully deleted !";
            }
        }
        return "Not Found ! ";
    }

    @Override
    public String deleteCommentLike(Comment comment, Long user) {
        if (comment != null) {
            Likes p = repo.findByUserAndComment(user, comment);
            if (p != null && p.getUser() == user) {
                repo.delete(p);
                return "Post was successfully deleted !";
            }
        }
        return "Not Found ! ";
    }

    @Override
    public List<Likes> findByUser(Long user) {
        return repo.findByUser(user);
    }

    @Override
    public Likes findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public LikesDTO convertToLikesDTO(Likes like) throws JsonProcessingException {
        LikesDTO likesDTO = new LikesDTO();
        likesDTO.setLikeId(like.getLikeId());
        likesDTO.setType(like.getType());
        likesDTO.setCreatedAt(like.getCreatedAt());
        likesDTO.setUser(userService.getUserDetailsFromId(like.getUser()));
        return likesDTO;
    }

    @Override
    public List<LikesDTO> convertToLikesDTOS(List<Likes> likes) throws JsonProcessingException {
        List<LikesDTO> likesDTOS = new ArrayList<>();
        List<Long> list = new ArrayList<>();
        for (Likes post : likes) {
            list.add(post.getUser());
        }
        List<UserDTO> users = userService.getUserDetailsFromIds(list);
        for (Likes like : likes){
            LikesDTO likesDTO = new LikesDTO();
            likesDTO.setLikeId(like.getLikeId());
            likesDTO.setType(like.getType());
            likesDTO.setCreatedAt(like.getCreatedAt());
            likesDTO.setUser(users.get(likes.indexOf(like)));
            likesDTOS.add(likesDTO);
        }
        return likesDTOS;
    }

}
