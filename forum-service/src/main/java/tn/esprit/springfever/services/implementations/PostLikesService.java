package tn.esprit.springfever.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.PostLike;
import tn.esprit.springfever.repositories.PostLikeRepository;
import tn.esprit.springfever.services.interfaces.IPostLikeService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostLikesService implements IPostLikeService {
    @Autowired
    private PostLikeRepository repo;
    @Override
    public PostLike addPostLike(PostLike like) {
        return repo.save(like);
    }

    @Override
    public PostLike updatePostLike(int id, PostLike like) {
        PostLike p = repo.findById(Long.valueOf(id)).orElse(null) ;
        if(p!=null) {
            like.setId(p.getId());
            repo.save(like);
        }
        return p;
    }

    @Override
    public String deletePostLike(int like) {
        PostLike p = repo.findById(Long.valueOf(like)).orElse(null) ;
        if(p!=null) {
            repo.delete(p);
        }
        return "Not Found ! ";
    }

    @Override
    public List<PostLike> getAllPostLikes() {
        return repo.findAll();
    }

    @Override
    public List<PostLike> getLikesByPosts(Post post) {
        return repo.findPostLikeByPost(post);
    }
}
