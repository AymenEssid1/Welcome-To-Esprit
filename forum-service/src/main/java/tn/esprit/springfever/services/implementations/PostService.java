package tn.esprit.springfever.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
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
import java.util.ArrayList;
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
    public List<Post> getAllLazy(int page, int size, HttpServletRequest requestt) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
        List<Post> list = pagerepo.findAll(pageable).getContent();
        String authHeader = requestt.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader!=null){
            String token = authHeader.substring("Bearer ".length());
            HttpHeaders headers = genericService.createHeadersWithBearerToken(token);
            HttpEntity<?> request = new HttpEntity<>("parameters",headers);
            try {
                ResponseEntity<?> response = restTemplate.exchange("http://localhost:8181/user/auth/id", HttpMethod.GET,
                        request, UserDTO.class);
                UserDTO userDTO = (UserDTO) response.getBody();
                if (userDTO !=null) {
                    String user = userDTO.getUsername();
                    list.forEach(post -> {
                        if (viewsRepository.findByPostAndUser(post, user) == null) {
                            viewsRepository.save(new PostViews(user, post, LocalDateTime.now()));
                        }
                    });
                }
            } catch (HttpClientErrorException ex) {
                log.error("Error calling user service1: {}", ex.getMessage());

            } catch (HttpServerErrorException ex) {
                log.error("Error calling user service2: {}", ex.getMessage());

            } catch (RestClientException ex) {
                log.error("Error calling user service3: {}", ex.getMessage());

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
