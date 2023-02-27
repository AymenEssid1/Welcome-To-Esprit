package tn.esprit.springfever.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.*;
import tn.esprit.springfever.repositories.PostPagingRepository;
import tn.esprit.springfever.repositories.PostRepository;
import tn.esprit.springfever.repositories.PostViewsRepository;
import tn.esprit.springfever.repositories.ReactionRepository;
import tn.esprit.springfever.services.interfaces.ILikesService;
import tn.esprit.springfever.services.interfaces.IMediaService;
import tn.esprit.springfever.services.interfaces.IPostService;
import tn.esprit.springfever.services.interfaces.IUserService;
import tn.esprit.springfever.utils.MediaComparator;
import tn.esprit.springfever.utils.MultipartFileSizeComparator;

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
    private IMediaService mediaService;

    @Autowired
    private ILikesService likesService;

    @Autowired
    private IUserService userService;

    @Autowired
    private MatchingService matchingService;

    @Autowired
    private ProfanitiesService profanitiesService;

    @Autowired
    private ReactionRepository reactionRepository;

    @Override
    public ResponseEntity<?> addPost(
            String title,
            String content,
            String topic,
            HttpServletRequest authentication,
            List<MultipartFile> images
    )
            throws JsonProcessingException {
        if (
                profanitiesService.containsBannedWords(topic) ||
                        profanitiesService.containsBannedWords(content) ||
                        profanitiesService.containsBannedWords(title)
        ) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(
                            "{\"Message\": \"Please check your post! We do not allow bad language\"}"
                    );
        } else if (
                authentication == null ||
                        authentication.getHeader(HttpHeaders.AUTHORIZATION) == null
        ) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("{\"Message\": \"Login or sign up to post!\"}");
        } else {
            Post p = new Post();
            p.setTitle(title);
            p.setContent(content);
            p.setTopic(topic);
            p.setCreatedAt(LocalDateTime.now());
            p.setUpdatedAt(LocalDateTime.now());
            p.setUser(
                    Long.valueOf(
                            userService
                                    .getUserDetailsFromToken(
                                            authentication.getHeader(HttpHeaders.AUTHORIZATION)
                                    )
                                    .getId()
                    )
            );
            if (images != null) {
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        try {
                            Media savedImageData = mediaService.save(image);
                            p.getMedia().add(savedImageData);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
            repo.save(p);
            return ResponseEntity.status(HttpStatus.CREATED).body(p);
        }
    }

    @Override
    @CachePut("post")
    public ResponseEntity<?> updatePost(
            Long id,
            String title,
            String content,
            String topic,
            HttpServletRequest authentication,
            List<MultipartFile> images
    )
            throws IOException {
        if (
                profanitiesService.containsBannedWords(topic) ||
                        profanitiesService.containsBannedWords(content) ||
                        profanitiesService.containsBannedWords(title)
        ) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(
                            "{\"Message\": \"Please check your post! We do not allow bad language\"}"
                    );
        } else if (
                authentication == null ||
                        authentication.getHeader(HttpHeaders.AUTHORIZATION) == null
        ) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("{\"Message\": \"Login or sign up to post!\"}");
        } else {
            Post p = repo.findById(Long.valueOf(id)).orElse(null);
            UserDTO user = null;
            if (
                    authentication != null &&
                            authentication.getHeader(HttpHeaders.AUTHORIZATION) != null
            ) {
                user =
                        userService.getUserDetailsFromToken(
                                authentication.getHeader(HttpHeaders.AUTHORIZATION)
                        );
                if (p != null) {
                    if (
                            user.getId() == p.getUser() ||
                                    user.getRoles().contains("FORUM_ADMIN") ||
                                    user.getRoles().contains("SUPER_ADMIN")
                    ) {
                        p.setUpdatedAt(LocalDateTime.now());
                        List<Media> mediaList = p.getMedia();
                        if (mediaList != null && images != null) {
                            Collections.sort(images, new MultipartFileSizeComparator());
                            Collections.sort(mediaList, new MediaComparator());
                            for (Media m : new ArrayList<>(mediaList)) {
                                for (MultipartFile f : new ArrayList<>(images)) {
                                    if (m.getContent().length == f.getBytes().length) {
                                        images.remove(f);
                                        mediaList.remove(m);
                                        break;
                                    }
                                }
                            }
                            for (Media m : mediaList) {
                                mediaService.delete(m.getMediaId());
                            }
                        }
                        if (images != null) {
                            if (!images.isEmpty()) {
                                for (MultipartFile image : images) {
                                    if (!image.isEmpty()) {
                                        try {
                                            Media savedImageData = mediaService.save(image);
                                            p.getMedia().add(savedImageData);
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
                        return ResponseEntity.ok().body(p);
                    } else {
                        return ResponseEntity
                                .status(HttpStatus.FORBIDDEN)
                                .body("{\"Message\": \"That's not yours to edit!\"}");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
            } else {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body("{\"Message\": \"Login or sign up to post!\"}");
            }
        }
    }

    @Override
    @CacheEvict("post")
    public String deletePost(Long post, HttpServletRequest authentication)
            throws IOException {
        Post p = repo.findById(Long.valueOf(post)).orElse(null);

        if (authentication != null) {
            UserDTO user = userService.getUserDetailsFromToken(
                    authentication.getHeader(HttpHeaders.AUTHORIZATION)
            );
            if (p != null) {
                if (
                        user.getId() == p.getUser() ||
                                user.getRoles().contains("FORUM_ADMIN") ||
                                user.getRoles().contains("SUPER_ADMIN")
                ) {
                    if (p.getMedia() != null) {
                        for (Media m : p.getMedia()) {
                            mediaService.delete(m.getMediaId());
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
        matchingService.addInterestsFromPost(request, post);
        return post;
    }

    @Override
    @Cacheable("post")
    public List<Post> getAllLazy(int page, int size, HttpServletRequest request) {
        PageRequest pageable = PageRequest.of(
                page,
                size
        );

        if (
                request == null || request.getHeader(HttpHeaders.AUTHORIZATION) == null
        ) {
            return pagerepo.findAll(pageable).getContent();
        } else {
            List<Post> list = matchingService.getPostsByUserInterests(pageable, request);
            this.incrementViews(request, list);
            for (Post p : list) {
                matchingService.addInterestsFromPost(request, p);
            }
            return list;
        }

    }

    @Override
    @Cacheable("post")
    public List<Post> getByUserLazy(
            int page,
            int size,
            Long id,
            HttpServletRequest request
    ) {
        PageRequest pageable = PageRequest.of(
                page,
                size
        );
        List<Post> list = pagerepo.findByUser(pageable, id).getContent();
        this.incrementViews(request, list);
        for (Post p : list) {
            matchingService.addInterestsFromPost(request, p);
        }
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
            for (Media postMedia : post.getMedia()) {
                SyndEnclosure enclosure = new SyndEnclosureImpl();
                enclosure.setUrl(
                        "https://www.myforum.com/posts/media/" + postMedia.getMediaId()
                );
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
                    String user = (String) userService
                            .getUserDetailsFromToken(authHeader)
                            .getUsername();
                    list.forEach(
                            post -> {
                                if (viewsRepository.findByPostAndUser(post, user) == null) {
                                    viewsRepository.save(
                                            new PostViews(user, post, LocalDateTime.now())
                                    );
                                }
                            }
                    );
                } catch (JsonProcessingException ex) {
                    log.error("Error calling user service1: {}", ex.getMessage());
                }
            }
        }
    }

    @Override
    @Cacheable("post")
    public List<Post> searchPosts(
            String searchString,
            int page,
            int size,
            HttpServletRequest request
    ) throws IOException {
        List<Post> posts = repo.findAll();
        List<Post> matchingPosts = new ArrayList<>();
        if (
                request == null || request.getHeader(HttpHeaders.AUTHORIZATION) == null
        ) {
            PageRequest pageable = PageRequest.of(
                    page,
                    size
            );
            for (Post post : posts) {
                if (
                        post.getTitle().contains(searchString) ||
                                post.getContent().contains(searchString) || post.getTitle().contains(searchString)
                ) {
                    matchingPosts.add(post);
                }
            }
        } else {
            matchingPosts =
                    matchingService.getPostsByAdvancedSearch(searchString, page, size);
            for (Post p : matchingPosts) {
                matchingService.addInterestsFromPost(request, p);
            }
            incrementViews(request, matchingPosts);
        }

        return matchingPosts;
    }

    @Override
    public Object likePost (Long reaction, Long post, HttpServletRequest request) throws JsonProcessingException {
        if (request != null && request.getHeader(HttpHeaders.AUTHORIZATION)!=null){
            UserDTO user = userService.getUserDetailsFromToken(request.getHeader(HttpHeaders.AUTHORIZATION));
            Likes like = new Likes();
            like.setUser(user.getId());
            like.setType(reactionRepository.findById(reaction).orElse(null));
            boolean liked = false;
            Post p = repo.findById(post).orElse(null);
            for (Likes pl : p.getLikes()){
                if(pl.getUser() == user.getId()){
                    liked = true;
                }
                log.warn("already liked!");
            }
            if (!liked){

                if (p!=null){
                    p.getLikes().add(like);
                    repo.save(p);
                    return likesService.addLike(like);
                }else{
                    return "The post you're trying to react to is not found!";
                }
            }else{
                return "You already reacted to this post!";
            }
        }else{
            return "You have to login to react to a post";
        }

    }

    @Override
    public Object changeReaction (Long id,Long reaction, HttpServletRequest request){
        Object ret = null;
        try {
            if (request != null && request.getHeader(HttpHeaders.AUTHORIZATION)!=null)
            ret = likesService.updatePostLike(id, reaction, userService.getUserDetailsFromToken(request.getHeader(HttpHeaders.AUTHORIZATION)).getId());
        }catch (Exception e){
            ret = "Error";
        }
        return ret;
    }

    @Override
    public String deleteReaction(Long id, HttpServletRequest request) throws JsonProcessingException {
        if (request == null || request.getHeader(HttpHeaders.AUTHORIZATION)==null){
            return "You have to login";
        }else{
            return  likesService.deletePostLike(id, userService.getUserDetailsFromToken(request.getHeader(HttpHeaders.AUTHORIZATION)).getId());
        }
    }
}
