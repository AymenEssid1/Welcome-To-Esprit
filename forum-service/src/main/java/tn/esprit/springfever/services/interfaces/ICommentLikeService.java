package tn.esprit.springfever.services.interfaces;

import tn.esprit.springfever.entities.CommentLike;

import java.util.List;

public interface ICommentLikeService {
    public CommentLike addCommentLike(CommentLike media);
    public CommentLike updateCommentLike(int id,CommentLike media);
    public String deleteCommentLike(int media);
    public List<CommentLike> getAllCommentLikes();
    public List<CommentLike> getLikesByComment(int comment);
}
