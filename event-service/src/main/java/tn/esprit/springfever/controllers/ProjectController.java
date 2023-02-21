package tn.esprit.springfever.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.DTO.ProjectDTO;
//import tn.esprit.springfever.Security.jwt.JwtUtils;
import tn.esprit.springfever.Services.Interfaces.IServiceProject;
import tn.esprit.springfever.entities.Project;
import tn.esprit.springfever.entities.Teams;

import java.io.IOException;
import java.util.List;


@RequestMapping("/Project")
@RestController(value = "/project")
@Api( tags = "project")


public class ProjectController {

    @Autowired
    IServiceProject iServiceProject;

    /*
    @Autowired
    private JwtUtils jwtUtils;
*/
    /*********  add project  ***********/
    @ApiOperation(value = "This method is used to add projects")

    @PostMapping("/add")
    @ResponseBody
    public Project addProject(@RequestBody Project project) throws IOException {return  iServiceProject.addProject(project);}


    /*********  update project  ***********/
    @PutMapping("/update/{idProject}")
    @ResponseBody
    public Project updateProject(@PathVariable Long idProject, @RequestBody ProjectDTO projectDTO )  {return  iServiceProject.updateProject(idProject , projectDTO);}


    /*********  get all project   ***********/
    @GetMapping("/getAllProject")
    @ResponseBody
    public List<Project> getAllProject()  {return  iServiceProject.getAllProject();}


    /*********  delete project  ***********/
    @DeleteMapping("/deleteProject/{idProject}")
    @ResponseBody
    public  boolean deleteProject(@PathVariable Long idProject)  {return  iServiceProject.deleteProject(idProject);}


}
