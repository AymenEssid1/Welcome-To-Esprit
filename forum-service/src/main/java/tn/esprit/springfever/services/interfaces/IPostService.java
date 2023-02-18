package tn.esprit.springfever.services.interfaces;

import tn.esprit.springfever.entities.Post;

import java.util.List;

public interface IPostService {
    public Post addPost(Post post);
    public Post updatePost(int id,Post post);
    public String deletePost(int post);
    public List<Post> getAllPosts();
}
