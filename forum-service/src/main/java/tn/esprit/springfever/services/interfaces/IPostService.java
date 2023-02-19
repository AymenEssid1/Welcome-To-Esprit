package tn.esprit.springfever.services.interfaces;

import tn.esprit.springfever.entities.Post;

import java.util.List;

public interface IPostService {
    public Post addPost(Post post);
    public Post updatePost(Long id,Post post);
    public String deletePost(Long post);
    public List<Post> getAllPosts();
    public Post getSinglePost(Long id);
    public List<Post> getByUser(Long id);
}
