package tn.esprit.springfever.services.interfaces;

import tn.esprit.springfever.entities.Comment;
import tn.esprit.springfever.entities.CommentLike;

import java.util.List;

public interface ICommentLikeService {
    public CommentLike addCommentLike(CommentLike like);
    public CommentLike updateCommentLike(Long id, Long type);
    public String deleteCommentLike(Long like);

}
