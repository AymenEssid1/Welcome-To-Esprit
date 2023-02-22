package tn.esprit.springfever.services.interfaces;

import tn.esprit.springfever.entities.CommentLike;

public interface ICommentLikeService {
    public CommentLike addCommentLike(CommentLike like);
    public CommentLike updateCommentLike(Long id, Long type);
    public String deleteCommentLike(Long like);

}
