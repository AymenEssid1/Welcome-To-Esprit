package tn.esprit.springfever.Services.Implementation;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;
import tn.esprit.springfever.entities.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.xhtmlrenderer.swing.Java2DRenderer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

@Service
public class BadgeGenerationService
{
    public String getHtml(String filePath) throws IOException {
        String htmlString = new String(Files.readAllBytes(Paths.get(filePath)));
        return htmlString;


    }
    public String changeHtml(User u) throws IOException {
// Parse the input HTML file using JSoup
        Document document = Jsoup.parse(new File(System.getProperty("user.dir")+"/assets/badge/badge.html"), "UTF-8");

        // Modify some p tags
        Element name = document.getElementById("name");

            // Modify the text of each p tag
            String nameText = u.getFirstname() + " "+u.getLastname();
            name.text(nameText);


        Element dob = document.getElementById("dob");

            String dobText = u.getDob().toString();
            dob.text(dobText);

        // Modify some image src attributes
        Element logo = document.getElementById("logo");

            // Modify the src attribute of each image
            String newSrc = System.getProperty("user.dir")+"/assets/espr-i.png";
            logo.attr("src", newSrc);

        Element img = document.getElementById("img");

            // Modify the src attribute of each image
            String imgsrc =u.getImage().getLocation();
            img.attr("src", imgsrc);

        Element qr = document.getElementById("qr");

            // Modify the src attribute of each image
            String qrsrc = System.getProperty("user.dir")+"/assets/"+u.getBadge().getQrCode().substring(u.getBadge().getQrCode().indexOf("assets/")+"assets/".length());;
            qr.attr("src", qrsrc);


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
        String outputFile=System.getProperty("user.dir")+"/assets/output.html";
        org.apache.commons.io.FileUtils.writeStringToFile(new File(outputFile), html, "UTF-8");
        return outputFile;
    }
    public String generate(User u) throws Exception {

        String pdf = System.getProperty("user.dir")+"/assets/badge-"+u.getUsername()+"-"+new Date().getTime()+".pdf";
        boolean success = HtmlToPdf.create()
                .object(HtmlToPdfObject.forHtml(getHtml(changeHtml(u))))
                .convert(pdf);
        if(success){
            return  pdf+"\n"+changeHtml(u);
        }
        else {
            return "error";
        }
    }
}
