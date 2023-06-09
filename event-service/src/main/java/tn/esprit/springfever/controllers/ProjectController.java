package tn.esprit.springfever.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.DTO.ProjectDTO;
//import tn.esprit.springfever.Security.jwt.JwtUtils;
import tn.esprit.springfever.Services.Implementation.FileLocationService;
import tn.esprit.springfever.Services.Interfaces.IServiceProject;
import tn.esprit.springfever.entities.Project;
import tn.esprit.springfever.entities.Teams;
import tn.esprit.springfever.entities.Video;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;


@RequestMapping("/Project")
@RestController(value = "/project")
@Api( tags = "project")


public class ProjectController {

    @Autowired
    FileLocationService fileLocationService;
    @Autowired
    IServiceProject iServiceProject;

    /*
    @Autowired
    private JwtUtils jwtUtils;
*/
    /*********  add project  ***********/
    @ApiOperation(value = "This method is used to add projects")


    @GetMapping(value = "/Getrapport/{id}")
    public ResponseEntity<FileSystemResource> downloadPDFrapport(@PathVariable("id") Long Id) {
        try {
            System.out.println("succes");
            FileSystemResource fileSystemResource = iServiceProject.findrapport(Id);
            System.out.println("succes");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(fileSystemResource);
        } catch (Exception e) {
            System.out.println("eror pdf");

            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/pdf-text/{id}")
    public String extractTextFromPdf(@PathVariable("id") Long Id) {
        return iServiceProject.extractTextFromPdf(Id);
    }


    @PostMapping(value="saveVideo",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //@ResponseBody
    public ResponseEntity<Project> addProject(@RequestParam("video") MultipartFile video,@RequestParam("rapport") MultipartFile rapportFile) throws Exception {
        //fileLocationService.saveVideo(video.getBytes(), video.getOriginalFilename());
        //Project saveProject = iServiceProject.addProject(project);
        try{
        Project saveRapport = iServiceProject.savef(rapportFile.getBytes(),rapportFile.getOriginalFilename(),video.getBytes(), video.getOriginalFilename());
        return  //new ResponseEntity<>(saveProject, (MultiValueMap<String, String>) saveRapport, HttpStatus.CREATED);
        ResponseEntity.status(HttpStatus.CREATED).body(saveRapport);}
        catch (Exception e){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR ).build();
        }



    }




    // let team uoload vd
    @PostMapping(value = "/{idProject}/uploadvideo",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadVideo(@PathVariable Long idProject, @RequestParam("video") MultipartFile video) {
        try {

            iServiceProject.uploadVideo(idProject, video);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    /*********  update project  ***********/
    @PutMapping("/update/{idProject}")
    @ResponseBody
    public ResponseEntity< Project> updateProject(@PathVariable("idProject") Long idProject, @RequestBody ProjectDTO projectDTO )  {
        Project updateProject = iServiceProject.updateProject(idProject,projectDTO);
        return  new ResponseEntity<>(updateProject,HttpStatus.OK);}


    /*********  get all project   ***********/
    @GetMapping("/getAllProject")
    @ResponseBody
    public ResponseEntity<List<Project>> getAllProject()  {
        List<Project> projects = iServiceProject.getAllProject();
        return new ResponseEntity<>(projects,HttpStatus.OK);}


    /*********  delete project  ***********/
    @DeleteMapping("/deleteProject/{idProject}")
    @ResponseBody
    public ResponseEntity<String> deleteProject(@PathVariable Long idProject)  {
        String result = iServiceProject.deleteProject(idProject);
        return  new ResponseEntity<>(result,HttpStatus.OK);}


    @PutMapping("assignNoteToProject/{idNote}/{idProject}")
    public String assignNoteToProject(@PathVariable("idNote") Long idNote, @PathVariable("idProject") Long idProject) {
        return iServiceProject.assignNoteToProject(idNote, idProject);
    }




    @GetMapping("/notes")
    public List<Object[]> getAllProjectsAndNotes() {
        return iServiceProject.getAllProjectsAndNotes();
    }


}
