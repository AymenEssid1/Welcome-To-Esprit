package tn.esprit.springfever.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.pdf.qrcode.WriterException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.DTO.TeamsDTO;
//import tn.esprit.springfever.Security.jwt.JwtUtils;
import tn.esprit.springfever.DTO.TeamsResponse;
import tn.esprit.springfever.Services.Interfaces.IFileLocationService;
import tn.esprit.springfever.Services.Interfaces.IServiceTeams;
import tn.esprit.springfever.entities.ImageData;
import tn.esprit.springfever.entities.Teams;
import tn.esprit.springfever.repositories.FileSystemRepository;
import tn.esprit.springfever.repositories.TeamsRepository;

import javax.mail.MessagingException;
import java.awt.*;
import java.io.DataInput;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@RequestMapping("/Teams")
@RestController(value = "/teams")
@Api( tags = "teams")

public class TeamsController {

    @Autowired
    IFileLocationService iFileLocationService;
    @Autowired
    IServiceTeams iServiceTeams;
    @Autowired
    TeamsRepository teamsRepository;
    @Autowired
    FileSystemRepository fileSystemRepository;
    /*
    @Autowired
    private JwtUtils jwtUtils;
*/
    /*********  add teams  ***********/
    @ApiOperation(value = "This method is used to add teams")

    @PostMapping( "/add")
    @ResponseBody
    public Teams addTeams(@RequestBody Teams teams) throws Exception {
        return  iServiceTeams.addTeams(teams);

       }


    @PutMapping("AssignImageToTeams/{idTeam}/{id}")
    public String AssignImageToTeams(@PathVariable("idTeam") Long idTeam ,@PathVariable("id") Long id ){
        return iServiceTeams.AssignImageToTeams(idTeam,id) ;

    }

    @PutMapping("AssignProjectToTeams/{idTeam}/{idProject}")
    public String AssignProjectToTeams(@PathVariable("idTeam") Long idTeam ,@PathVariable("idProject") Long idProject ){
        return iServiceTeams.AssignProjectToTeams(idTeam,idProject) ;

    }


    /*********  update teams  ***********/
    @PutMapping("/update/{idTeam}")
    @ResponseBody
    public Teams updateTeams(@PathVariable Long idTeam, @RequestBody TeamsDTO teamsDTO )  {return  iServiceTeams.updateTeams(idTeam , teamsDTO);}


    /*********  get all teams   ***********/
    @GetMapping("/getAllTeams")
    @ResponseBody
    public List<TeamsResponse> getAllTeams() throws JsonProcessingException {return  iServiceTeams.getAllTeams();}


    /*********  delete teams  ***********/
    @DeleteMapping("/deleteTeams/{idTeam}")
    @ResponseBody
    public  boolean deleteTeams(@PathVariable Long idTeam)  {return  iServiceTeams.deleteTeams(idTeam);}


    @PostMapping("/assign-users-and-send-mail")
    public ResponseEntity<Void> affectusertoteams() throws JsonProcessingException {
        iServiceTeams.assignUserToTeams();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sendInvitation")
    public ResponseEntity<String> sendInvitation() throws JsonProcessingException {
        try {
            iServiceTeams.sendOnlineEventInvitation();
            return ResponseEntity.ok("Invitation sent successfully");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send invitation: " + e.getMessage());
        }
    }
    public void sendOnlineEventInvitation() throws MessagingException, JsonProcessingException {
        String recipient = "nour.yahyaoui@esprit.tn";
        String googleMeetLink = "https://meet.google.com/odn-qtfp-tcs";
        iServiceTeams.sendOnlineEventInvitation();
    }

/*
    @GetMapping(value = "/teams/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<FileSystemResource> downloadImage(@PathVariable Long id) {
        try {
            Teams teams = teamsRepository.findById(id).orElse(null);
            FileSystemResource fileSystemResource = fileSystemRepository.findInFileSystem(teams.getQRcertificat() );
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(fileSystemResource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
*/

    @GetMapping("/{id}")
    public String getTeamById(@PathVariable Long id, Model model) {
        Optional<Teams> team = iServiceTeams.getTeamById(id);
        if (team.isPresent()) {

            return "team";
        } else {
            return "error";
        }
    }

    @PostMapping("/")
    public String saveTeam(@RequestParam String nameTeam, Model model) {
        Teams team = new Teams();
        Teams savedTeam = iServiceTeams.saveTeam(team);
        //model.addAttribute("team", savedTeam);
        return "team";
    }

    @GetMapping("/{id}/qrcode")
    @ResponseBody
    public ResponseEntity<byte[]> getQRCodeForTeam(@PathVariable Long id) {
        Optional<Teams> team = iServiceTeams.getTeamById(id);
        if (team.isPresent()) {
            byte[] qrCodeBytes;
            try {
                qrCodeBytes = iServiceTeams.generateQRCode(team.get().getNameTeam() , "Certificate of Participation\n" +
                        "\n" +
                        "This is to certify that "+team.get().getNameTeam()+" has participated in the APP0 held on 13-03-2023 and has successfully completed all the requirements for the event.\n" +
                        "\n" +
                        "We wish "+team.get().getNameTeam()+" all the best in their future endeavors and hope that this certificate serves as a testament to their hard work and achievement.\n" +
                        "\n" +
                        "Congratulations on a job well done! We appreciate your hard work and dedication to your projects !\n");



                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG);
                headers.setContentLength(qrCodeBytes.length);
                headers.setContentDispositionFormData("attachment", team.get().getNameTeam() + ".png");
                headers.setCacheControl("no-cache, no-store, must-revalidate");
                return new ResponseEntity<byte[]>(qrCodeBytes, headers, HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (com.google.zxing.WriterException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
        }
    }


}
