package tn.esprit.springfever.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.repositories.PostLikeRepository;
import tn.esprit.springfever.repositories.PostRepository;
import tn.esprit.springfever.services.interfaces.IPostService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostService implements IPostService {

    @Autowired
    private PostRepository repo;
    @Override
    public Post addPost(Post post) {
        return repo.save(post);
    }

    @Override
    public Post updatePost(int id, Post post) {
        Post p = repo.findById(Long.valueOf(id)).orElse(null) ;
        if(p!=null) {
            post.setId(p.getId());
            repo.save(post);
        }
        return p;
    }

    @Override
    public String deletePost(int post) {
        Post p = repo.findById(Long.valueOf(post)).orElse(null) ;
        if(p!=null) {
            repo.delete(p);
            return "Post was successfully deleted !" ;
        }
        return "Not Found ! ";

    }

    @Override
    public List<Post> getAllPosts() {
        return repo.findAll();
    }
}
