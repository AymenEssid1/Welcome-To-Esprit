package tn.esprit.springfever.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.DTO.TeamsDTO;
//import tn.esprit.springfever.Security.jwt.JwtUtils;
import tn.esprit.springfever.Services.Interfaces.IFileLocationService;
import tn.esprit.springfever.Services.Interfaces.IServiceTeams;
import tn.esprit.springfever.entities.ImageData;
import tn.esprit.springfever.entities.Teams;

import java.awt.*;
import java.io.DataInput;
import java.io.IOException;
import java.util.List;


@RequestMapping("/Teams")
@RestController(value = "/teams")
@Api( tags = "teams")

public class TeamsController {

    @Autowired
    IFileLocationService iFileLocationService;
    @Autowired
    IServiceTeams iServiceTeams;
    /*
    @Autowired
    private JwtUtils jwtUtils;
*/
    /*********  add teams  ***********/
    @ApiOperation(value = "This method is used to add teams")

    @PostMapping(value = "/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE , produces = "application/json")
    @ResponseBody
    public ResponseEntity<Teams> addTeams(@RequestParam Teams teams,@RequestParam("image") MultipartFile image) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        TeamsDTO teamsDTO = objectMapper.readValue((DataInput) teams,TeamsDTO.class);

        Teams t=new Teams();
        t.setNameTeam(teamsDTO.getNameTeam());
        t.setQRcertificat(teamsDTO.getQRcertificat());
        t.setNiveauEtude(teamsDTO.getNiveauEtude());

        if(image!=null){
            System.out.println(image.getOriginalFilename());
            ImageData newImage = iFileLocationService.save(image);
            t.setImage(newImage);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(t);
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
    public List<Teams> getAllTeams()  {return  iServiceTeams.getAllTeams();}


    /*********  delete teams  ***********/
    @DeleteMapping("/deleteTeams/{idTeam}")
    @ResponseBody
    public  boolean deleteTeams(@PathVariable Long idTeam)  {return  iServiceTeams.deleteTeams(idTeam);}


    @PostMapping("/assign-users")
    public ResponseEntity<Void> affectusertoteams() {
        iServiceTeams.assignUserToTeams();
        return ResponseEntity.ok().build();
    }



}
