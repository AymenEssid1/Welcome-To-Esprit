package tn.esprit.springfever.services.interfaces;

import tn.esprit.springfever.entities.Comment;
import tn.esprit.springfever.entities.Post;

import java.util.List;

public interface ICommentService {
    public Comment addComment(Comment post);
    public Comment updateComment(Long id,Comment comment);
    public String deleteComment(Long comment);
    public List<Comment> getAllComments();
    public List<Comment> getCommentsByPosts(Post post);
}
