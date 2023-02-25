package tn.esprit.springfever.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.PostMedia;
import tn.esprit.springfever.entities.PostViews;
import tn.esprit.springfever.repositories.PostPagingRepository;
import tn.esprit.springfever.repositories.PostRepository;
import tn.esprit.springfever.repositories.PostViewsRepository;
import tn.esprit.springfever.services.interfaces.IPostService;
import org.springframework.security.core.userdetails.UserDetails;
import tn.esprit.springfever.services.interfaces.IUserService;
import tn.esprit.springfever.services.interprocess.RabbitMQMessageSender;
import tn.esprit.springfever.utils.MultipartFileSizeComparator;
import tn.esprit.springfever.utils.PostMediaComparator;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
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
    @Autowired
    private PostMediaService mediaService;
    @Autowired
    private IUserService userService;


    @Override
    public Post addPost(String title, String content, String topic, HttpServletRequest authentication, List<MultipartFile> images) throws JsonProcessingException {
        Post p = new Post();
        p.setTitle(title);
        p.setContent(content);
        p.setTopic(topic);
        p.setCreatedAt(LocalDateTime.now());
        p.setUpdatedAt(LocalDateTime.now());
        p.setUser(Long.valueOf(userService.getUserDetailsFromToken(authentication.getHeader(HttpHeaders.AUTHORIZATION)).getId()));
        repo.save(p);
        if (images != null) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    try {
                        PostMedia savedImageData = mediaService.save(image, p);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        return p;
    }

    @Override
    @CachePut("post")
    public Post updatePost(Long id, String title, String content, String topic, HttpServletRequest authentication, List<MultipartFile> images) throws IOException {
        Post p = repo.findById(Long.valueOf(id)).orElse(null);
        UserDTO user = null;
        if (authentication != null && authentication.getHeader(HttpHeaders.AUTHORIZATION) != null) {
            user = userService.getUserDetailsFromToken(authentication.getHeader(HttpHeaders.AUTHORIZATION));
            if (p != null) {
                if (user.getId() == p.getUser() || user.getRoles().contains("FORUM_ADMIN") || user.getRoles().contains("SUPER_ADMIN")) {
                    p.setUpdatedAt(LocalDateTime.now());
                    List<PostMedia> mediaList = p.getMedia();
                    if (mediaList != null && images != null) {
                        Collections.sort(images, new MultipartFileSizeComparator());
                        Collections.sort(mediaList, new PostMediaComparator());
                        for (PostMedia m : new ArrayList<>(mediaList)) {
                            for (MultipartFile f : new ArrayList<>(images)) {
                                if (m.getContent().length == f.getBytes().length) {
                                    images.remove(f);
                                    mediaList.remove(m);
                                    break;
                                }
                            }

                        }
                        for (PostMedia m : mediaList) {
                            mediaService.delete(m.getId());
                        }
                    }
                    if (images != null) {
                        if (!images.isEmpty()) {
                            for (MultipartFile image : images) {
                                if (!image.isEmpty()) {
                                    try {
                                        PostMedia savedImageData = mediaService.save(image, p);
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                            }
                        }

                    }
                    if (title != null) {
                        p.setTitle(title);
                    }
                    if (content != null) {
                        p.setContent(content);
                    }
                    if (topic != null) {
                        p.setTopic(topic);
                    }
                    repo.save(p);
                    return p;
                } else {
                    return null;
                }
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    @Override
    @CacheEvict("post")
    public String deletePost(Long post, HttpServletRequest authentication) throws IOException {
        Post p = repo.findById(Long.valueOf(post)).orElse(null);

            if (authentication != null) {
                UserDTO user = userService.getUserDetailsFromToken(authentication.getHeader(HttpHeaders.AUTHORIZATION));
                if (p != null) {
                    if (user.getId() == p.getUser() || user.getRoles().contains("FORUM_ADMIN") || user.getRoles().contains("SUPER_ADMIN")) {
                        if (p.getMedia() != null) {
                            for (PostMedia m : p.getMedia()) {
                                mediaService.delete(m.getId());
                            }
                        }
                        repo.delete(p);
                        return "Post deleted successfully";
                    } else {
                        return "You can't delete this post!";
                    }
                } else {
                    return "Post not found!";
                }
            } else {
                return "Log in to delete th post!";
            }


    }

    @Override
    @Cacheable("post")
    public Post getSinglePost(Long id, HttpServletRequest request) {
        Post post = repo.findById(id).orElse(null);
        List<Post> list = new ArrayList<>();
        list.add(post);
        incrementViews(request, list);
        return post;
    }

    @Override
    @Cacheable("post")
    public List<Post> getAllLazy(int page, int size, HttpServletRequest request) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
        List<Post> list = pagerepo.findAll(pageable).getContent();
        this.incrementViews(request, list);
        return list;

    }

    @Override
    @Cacheable("post")
    public List<Post> getByUserLazy(int page, int size, Long id, HttpServletRequest request) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
        List<Post> list = pagerepo.findByUser(pageable, id).getContent();
        this.incrementViews(request, list);
        return list;
    }

    @Override
    public String rssFeed() throws FeedException {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        List<Post> list = pagerepo.findAll(pageable).getContent();
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle("My Forum RSS Feed");
        feed.setDescription("Latest posts from the My Forum website");
        feed.setLink("http://localhost:8181/forum");
        List<SyndEntry> entries = new ArrayList<>();
        for (Post post : list) {
            SyndEntry entry = new SyndEntryImpl();
            entry.setTitle(post.getTopic() + ": " + post.getTitle());
            entry.setLink("http://localhost:8181/forum/posts/" + post.getId());
            //entry.setPublishedDate(Date.from(post.getTimestamps().atZone(ZoneId.systemDefault()).toInstant()));
            SyndContent description = new SyndContentImpl();
            description.setType("text/plain");
            description.setValue(post.getContent());
            entry.setDescription(description);
            List<SyndEnclosure> enclosures = new ArrayList<>();
            for (PostMedia postMedia : post.getMedia()) {
                SyndEnclosure enclosure = new SyndEnclosureImpl();
                enclosure.setUrl("https://www.myforum.com/posts/media/" + postMedia.getId());
                enclosure.setType(postMedia.getType());
                enclosure.setLength(postMedia.getContent().length);
                enclosures.add(enclosure);
            }
            entry.setEnclosures(enclosures);
            entries.add(entry);
        }
        feed.setEntries(entries);
        String rss = new SyndFeedOutput().outputString(feed);
        return rss;
    }

    public void incrementViews(HttpServletRequest request, List<Post> list) {
        if (request != null) {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader != null) {
                try {
                    String user = (String) userService.getUserDetailsFromToken(authHeader).getUsername();
                    list.forEach(post -> {
                        if (viewsRepository.findByPostAndUser(post, user) == null) {
                            viewsRepository.save(new PostViews(user, post, LocalDateTime.now()));
                        }
                    });
                } catch (JsonProcessingException ex) {
                    log.error("Error calling user service1: {}", ex.getMessage());
                }
            }
        }
    }


}
