package tn.esprit.springfever.Services.Interfaces;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.DTO.ProjectDTO;
import tn.esprit.springfever.entities.Project;

import tn.esprit.springfever.entities.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
public interface IServiceProject {
    public Project addProject(Project project) throws IOException;
    public  List<Project> getAllProject() ;
    public  String deleteProject(Long idProject) ;
    public Project updateProject (Long idProject , ProjectDTO projectDTO) ;
    public void uploadVideo(Long idProject,MultipartFile video)throws IOException;
}
