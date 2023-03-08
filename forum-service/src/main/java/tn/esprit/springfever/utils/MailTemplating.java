package tn.esprit.springfever.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import org.xhtmlrenderer.swing.Java2DRenderer;
import tn.esprit.springfever.dto.PostDTO;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

@Service
@Slf4j
public class MailTemplating {
    @Value("spring.application.link")
    String applicationLink;

    public String getHtml(String filePath) throws IOException {
        String htmlString = new String(Files.readAllBytes(Paths.get(filePath)));
        File file = new File(filePath);
        try {
            file.delete();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return htmlString;


    }

    public String changeHtml(PostDTO post) {
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
