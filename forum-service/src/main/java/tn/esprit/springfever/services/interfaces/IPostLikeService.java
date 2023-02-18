package tn.esprit.springfever.services.interfaces;

import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.PostLike;

import java.util.List;

public interface IPostLikeService {
    public PostLike addPostLike(PostLike like);
    public PostLike updatePostLike(int id,PostLike like);
    public String deletePostLike(int like);
    public List<PostLike> getAllPostLikes();
    public List<PostLike> getLikesByPosts(Post post);
}
