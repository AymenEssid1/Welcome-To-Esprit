package tn.esprit.springfever.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.repositories.PostPagingRepository;
import tn.esprit.springfever.repositories.PostRepository;
import tn.esprit.springfever.services.interfaces.IPostService;

import java.util.List;

@Service
@Slf4j
public class PostService implements IPostService {

    @Autowired
    private PostRepository repo;

    @Autowired
    private PostPagingRepository pagerepo;
    @Override
    public Post addPost(Post post) {
        return repo.save(post);
    }

    @Override
    public Post updatePost(Long id, Post post) {
        Post p = repo.findById(Long.valueOf(id)).orElse(null) ;
        if(p!=null) {
            post.setId(p.getId());
            repo.save(post);
        }
        return p;
    }

    @Override
    public String deletePost(Long post) {
        Post p = repo.findById(Long.valueOf(post)).orElse(null) ;
        if(p!=null) {
            repo.delete(p);
            return "Post was successfully deleted !" ;
        }
        return "Not Found ! ";

    }

    @Override
    public Post getSinglePost(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public List<Post> getAllLazy(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return  pagerepo.findAll(pageable).getContent();

    }

    @Override
    public List<Post> getByUserLazy(int page, int size, Long id) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return pagerepo.findByUser(pageable, id).getContent();
    }
}
