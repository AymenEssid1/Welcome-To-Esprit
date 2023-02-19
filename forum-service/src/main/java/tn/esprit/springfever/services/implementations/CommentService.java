package tn.esprit.springfever.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.entities.Comment;
import tn.esprit.springfever.entities.CommentLike;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.repositories.CommentRepository;
import tn.esprit.springfever.services.interfaces.ICommentService;

import java.util.List;

@Service
@Slf4j
public class CommentService implements ICommentService {
    @Autowired
    private CommentRepository repo;
    @Override
    public Comment addComment(Comment comment) {
        return repo.save(comment);
    }

    @Override
    public Comment updateComment(Long id, Comment comment) {
        Comment p = repo.findById(Long.valueOf(id)).orElse(null) ;
        if(p!=null) {
            comment.setId(p.getId());
            repo.save(comment);
        }
        return p;
    }

    @Override
    public String deleteComment(Long comment) {
        Comment p = repo.findById(Long.valueOf(comment)).orElse(null) ;
        if(p!=null) {
            repo.delete(p);
            return "Post was successfully deleted !" ;
        }
        return "Not Found ! ";
    }

    @Override
    public List<Comment> getAllComments() {
        return repo.findAll();
    }

    public List<Comment> getCommentsByPosts(Post id) {
        return repo.findByPost(id);
    }
}
