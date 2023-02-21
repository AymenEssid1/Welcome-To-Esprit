package tn.esprit.springfever.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
        if (repo.findByUser(like.getUser()).isEmpty()){
            return repo.save(like);
        }
        return null;
    }

    @Override
    @CachePut("postLike")
    public PostLike updatePostLike(Long id, PostLike like) {
        PostLike p = repo.findById(Long.valueOf(id)).orElse(null) ;
        if(p!=null) {
            like.setId(p.getId());
            repo.save(like);
        }
        return p;
    }

    @Override
    @CacheEvict("postLike")
    public String deletePostLike(Long like) {
        PostLike p = repo.findById(Long.valueOf(like)).orElse(null) ;
        if(p!=null) {
            repo.delete(p);
        }
        return "Not Found ! ";
    }

    @Override
    @Cacheable("postLike")
    public List<PostLike> getAllPostLikes() {
        return repo.findAll();
    }

    @Override
    @Cacheable("postLike")
    public List<PostLike> getLikesByPosts(Post post) {
        return repo.findPostLikeByPost(post);
    }
}
