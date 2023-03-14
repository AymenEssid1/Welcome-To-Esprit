package tn.esprit.springfever.batch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import tn.esprit.springfever.configuration.MailConfiguration;
import tn.esprit.springfever.dto.PostDTO;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.UserInterest;
import tn.esprit.springfever.repositories.PostRepository;
import tn.esprit.springfever.services.implementations.MatchingService;
import tn.esprit.springfever.services.implementations.UserService;
import tn.esprit.springfever.utils.MailTemplating;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BatchJob {

    @Autowired
    private RabbitTemplate amqpTemplate;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MatchingService matchingService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    @Value("${spring.rabbitmq.template.routing-key.forum.daily}")
    private String routingKeyForumIds;
    @Autowired
    private MailConfiguration mailConfiguration;

    @Autowired
    private MailTemplating mailTemplating;

    @Autowired
    private UserService userService;

    @Value("${spring.application.link}")
    String applicationLink;


    @Cacheable("user")
    @RabbitListener(bindings = {
            @QueueBinding(value =
            @Queue(value = "${spring.rabbitmq.template.queue.forum}"), exchange = @Exchange("${spring.rabbitmq.template.exchange.forum}"), key = "${spring.rabbitmq.template.routing-key.forum.daily}")})
    public void receiveMessage(@Payload Message message, @Header("amqp_receivedRoutingKey") String routingKey) throws IOException {
        if (routingKey.equals(routingKeyForumIds)) {
            String token = new String(message.getBody(), StandardCharsets.UTF_8);
            log.info(token);
            sendDailyMaill(message);
        }
    }

    public void sendDailyMaill(Message message) throws JsonProcessingException {
        String token = new String(message.getBody(), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        List<UserDTO> list = objectMapper.readValue(token, new TypeReference<List<UserDTO>>() {
        });
        for (UserDTO user : list) {
            List<Post> posts = postRepository.findUnviewedPosts(user.getId());
            Set<UserInterest> interests = new HashSet<UserInterest>(matchingService.findUserInterests(user.getId()));
            List<String> userInterests = interests.stream()
                    .map(UserInterest::getTopic)
                    .collect(Collectors.toList());
            List<Double> userInterestVector = matchingService.getVector(userInterests);
            if (posts.size() > 0) {
                for (Post p : posts) {
                    List<String> t = matchingService.extractTopicsFromPost(p.getTopic() + " " + p.getContent() + " " + p.getTitle());
                    List<Double> postVector = matchingService.getVector(t);
                    p.setSimilarity(matchingService.getCosineSimilarity(postVector, userInterestVector));
                }
                Collections.sort(posts, Collections.reverseOrder(Comparator.comparingDouble(Post::getSimilarity)));
                try {
                    UserDTO postOwner = userService.getUserDetailsFromId(posts.get(0).getUser());
                    MimeMessage email = mailConfiguration.getJavaMailSender().createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(email, true);
                    helper.setSubject("Check out this post by: " + " " + (postOwner.getUsername()));
                    helper.setText(mailTemplating.getHtml(changeHtml(posts.get(0))), true);

                    helper.setTo(user.getEmail());
                    mailConfiguration.sendEmail(email);


                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }

        }

    }

    public String changeHtml(Post post) {
// Parse the input HTML file using JSoup
        Document document = null;
        try {
            document = Jsoup.parse(new File(System.getProperty("user.dir") + "/forum-service/src/main/resources/assets/email.html"), "UTF-8");

            // Modify some p tags
            Element name = document.getElementById("title");
            // Modify the text of each p tag
            String nameText = post.getTitle();
            name.text(nameText);
            Element dob = document.getElementById("post");
            String delimiter = "[.?!,:]";
            String[] sentences = post.getContent().split(delimiter);
            String result = sentences[0] + " " + sentences[1];
            String dobText = result + " ...";
            dob.text(dobText);
            Element link = document.getElementById("link");
            String linkText = applicationLink + "/forum/posts/" + post.getId();
            link.attr("href", linkText);

            Element forumLink = document.getElementById("forumLink");
            String forumLinkText = applicationLink + "/forum/posts/?page=0&size=10";
            forumLink.attr("href", forumLinkText);


            // Write the modified HTML to a new file
            document.outputSettings().prettyPrint(false);
            document.charset(StandardCharsets.UTF_8);
            document.outputSettings().escapeMode(org.jsoup.nodes.Entities.EscapeMode.base);
            document.outputSettings().charset("UTF-8");
            document.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.html);
            document.outputSettings().indentAmount(0);
            document.outputSettings().outline(false);
            //document.outputSettings().appendToBody("");
            document.outputSettings().prettyPrint(false);

            String html = document.html();
            String outputFile = System.getProperty("user.dir") + "/assets/output.html";
            org.apache.commons.io.FileUtils.writeStringToFile(new File(outputFile), html, "UTF-8");
            return outputFile;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
