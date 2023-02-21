package tn.esprit.springfever.services.interfaces;

import tn.esprit.springfever.entities.Comment;

public interface ICommentService {
    public Comment addComment(Comment comment);
    public Comment updateComment(Long id,Comment comment);
    public String deleteComment(Long comment);
    public Comment getSingleComment(Long id);
}
