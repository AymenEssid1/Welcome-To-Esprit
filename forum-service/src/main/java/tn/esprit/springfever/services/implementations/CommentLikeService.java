package tn.esprit.springfever.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.entities.Comment;
import tn.esprit.springfever.entities.CommentLike;
import tn.esprit.springfever.entities.Reaction;
import tn.esprit.springfever.repositories.CommentLikeRepository;
import tn.esprit.springfever.repositories.ReactionRepository;
import tn.esprit.springfever.services.interfaces.ICommentLikeService;

import java.util.List;

@Service
@Slf4j
public class CommentLikeService implements ICommentLikeService {
    @Autowired
    private CommentLikeRepository repo;
    @Autowired
    private ReactionRepository reactionRepository;
    @Override
    public CommentLike addCommentLike(CommentLike like) {
        return repo.save(like);
    }

    @Override
    @CachePut("commentLike")
    public CommentLike updateCommentLike(Long id, Long type) {
        CommentLike p = repo.findById(Long.valueOf(id)).orElse(null) ;
        Reaction r = reactionRepository.findById(type).orElse(null);
        if(p!=null) {
            if (r!=null){
                p.setType(r);
            }
        }
        return p;
    }

    @Override
    @CacheEvict("commentLike")
    public String deleteCommentLike(Long id) {
        CommentLike p = repo.findById(Long.valueOf(id)).orElse(null) ;
        if(p!=null) {
            repo.delete(p);
            return "Post was successfully deleted !" ;
        }
        return "Not Found ! ";
    }

}
