package tn.esprit.springfever.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.entities.CommentLike;
import tn.esprit.springfever.repositories.CommentLikeRepository;
import tn.esprit.springfever.services.interfaces.ICommentLikeService;

import java.util.List;

@Service
@Slf4j
public class CommentLikeService implements ICommentLikeService {
    @Autowired
    private CommentLikeRepository repo;
    @Override
    public CommentLike addCommentLike(CommentLike like) {
        return repo.save(like);
    }

    @Override
    public CommentLike updateCommentLike(int id, CommentLike like) {
        CommentLike p = repo.findById(Long.valueOf(id)).orElse(null) ;
        if(p!=null) {
            like.setId(p.getId());
            repo.save(like);
        }
        return p;
    }

    @Override
    public String deleteCommentLike(int id) {
        CommentLike p = repo.findById(Long.valueOf(id)).orElse(null) ;
        if(p!=null) {
            repo.delete(p);
            return "Post was successfully deleted !" ;
        }
        return "Not Found ! ";
    }

    @Override
    public List<CommentLike> getAllCommentLikes() {
        return repo.findAll();
    }

    @Override
    public List<CommentLike> getLikesByComment(int comment) {
        return repo.findLikeByComment(comment);
    }
}
