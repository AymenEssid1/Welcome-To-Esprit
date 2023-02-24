package tn.esprit.springfever.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.DTO.TeamsDTO;
//import tn.esprit.springfever.Security.jwt.JwtUtils;
import tn.esprit.springfever.Services.Interfaces.IServiceTeams;
import tn.esprit.springfever.entities.Teams;

import java.io.IOException;
import java.util.List;


@RequestMapping("/Teams")
@RestController(value = "/teams")
@Api( tags = "teams")

public class TeamsController {


    @Autowired
    IServiceTeams iServiceTeams;
    /*
    @Autowired
    private JwtUtils jwtUtils;
*/
    /*********  add teams  ***********/
    @ApiOperation(value = "This method is used to add teams")

    @PostMapping("/add")
    @ResponseBody
    public Teams addTeams(@RequestBody Teams teams) throws IOException {return  iServiceTeams.addTeams(teams);}


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
