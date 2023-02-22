package tn.esprit.springfever.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.PostViews;
import tn.esprit.springfever.repositories.PostPagingRepository;
import tn.esprit.springfever.repositories.PostRepository;
import tn.esprit.springfever.repositories.PostViewsRepository;
import tn.esprit.springfever.security.UserPrincipal;
import tn.esprit.springfever.services.interfaces.IGenericService;
import tn.esprit.springfever.services.interfaces.IPostService;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PostService implements IPostService {

    @Autowired
    private PostRepository repo;
    @Autowired
    private PostPagingRepository pagerepo;
    @Autowired
    private PostViewsRepository viewsRepository;
    private RestTemplate restTemplate = new RestTemplate();
    private GenericService  genericService= new GenericService();


    @Override
    public Post addPost(Post post) {
        return repo.save(post);
    }

    @Override
    @CachePut("post")
    public Post updatePost(Long id, Post post) {
        Post p = repo.findById(Long.valueOf(id)).orElse(null);
        if (p != null) {
            post.setId(p.getId());
            repo.save(post);
        }
        return p;
    }

    @Override
    @CacheEvict("post")
    public String deletePost(Long post) {
        Post p = repo.findById(Long.valueOf(post)).orElse(null);
        if (p != null) {
            repo.delete(p);
            return "Post was successfully deleted !";
        }
        return "Not Found ! ";

    }

    @Override
    @Cacheable("post")
    public Post getSinglePost(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public List<Post> getAllLazy(int page, int size, HttpServletRequest request) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
        List<Post> list = pagerepo.findAll(pageable).getContent();
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info(authHeader);
        if (authHeader!=null){
            String token = authHeader.substring("Bearer ".length());
            ObjectMapper objectMapper = new ObjectMapper();
            JSONObject response = restTemplate.exchange("http://localhost:8181/user/auth/id", HttpMethod.GET,
                    new HttpEntity<>(null, genericService.createHeadersWithBearerToken(token)), JSONObject.class).getBody();
            UserDTO userDTO = null;
            try {
                userDTO = objectMapper.readValue(response.toString(), UserDTO.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            if (userDTO !=null) {
                log.info(userDTO.getUsername());
                String user = userDTO.getUsername();
                list.forEach(post -> {
                    if (viewsRepository.findByPostAndUser(post, user) == null) {
                        viewsRepository.save(new PostViews(user, post, LocalDateTime.now()));
                    }
                    ;
                });
            }
        }

        return list;

    }

    @Override
    @Cacheable("post")
    public List<Post> getByUserLazy(int page, int size, Long id) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return pagerepo.findByUser(pageable, id).getContent();
    }


}
