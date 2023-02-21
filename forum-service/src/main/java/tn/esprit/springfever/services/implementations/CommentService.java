package tn.esprit.springfever.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.entities.*;
import tn.esprit.springfever.repositories.CommentPagingRepository;
import tn.esprit.springfever.repositories.CommentRepository;
import tn.esprit.springfever.services.interfaces.ICommentService;

@Service
@Slf4j
public class CommentService implements ICommentService {
    @Autowired
    private CommentRepository repo;

    @Autowired
    private CommentPagingRepository pagingRepository;
    @Override
    public Comment addComment(Comment comment) {
        return repo.save(comment);
    }

    @Override
    @CachePut("comment")
    public Comment updateComment(Long id, Comment comment) {
        Comment p = repo.findById(Long.valueOf(id)).orElse(null) ;
        if(p!=null) {
            comment.setId(p.getId());
            repo.save(comment);
        }
        return p;
    }

    @Override
    @CacheEvict("comment")
    public String deleteComment(Long comment) {
        Comment p = repo.findById(Long.valueOf(comment)).orElse(null) ;
        if(p!=null) {
            repo.delete(p);
            return "Post was successfully deleted !" ;
        }
        return "Not Found ! ";
    }

    @Override
    @Cacheable("comment")
    public Comment getSingleComment(Long id) {
        return repo.findById(id).orElse(null);
    }

}
