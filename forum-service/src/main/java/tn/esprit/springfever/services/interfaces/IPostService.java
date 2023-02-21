package tn.esprit.springfever.services.interfaces;

import tn.esprit.springfever.entities.Post;

import java.util.List;

public interface IPostService {
    public Post addPost(Post post);
    public Post updatePost(Long id,Post post);
    public String deletePost(Long post);
    public Post getSinglePost(Long id);
    public List<Post> getAllLazy(int skip, int take);
    public List<Post> getByUserLazy(int skip, int take, Long id);
}
