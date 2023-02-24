package tn.esprit.springfever.Services.Implementation;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.DTO.ProjectDTO;
import tn.esprit.springfever.DTO.TeamsDTO;
import tn.esprit.springfever.Services.Interfaces.ProjectMapper;
import tn.esprit.springfever.Services.Interfaces.IServiceProject;
import tn.esprit.springfever.entities.Event;
import tn.esprit.springfever.entities.Project;
import tn.esprit.springfever.entities.Teams;
import tn.esprit.springfever.entities.User;
import tn.esprit.springfever.repositories.ProjectRepository;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class ServiceProjectImpl implements IServiceProject{

    @Autowired
    ProjectRepository projectRepository ;
    @Autowired
    ProjectMapper projectMapper;

    @Override
    public Project addProject(Project project){
        log.info("project was successfully added !");
        return projectRepository.save(project);
    }

/*
    @Override
    public Project addProject(Project project) throws IOException {

        // Load the tokenizer model
        InputStream modelIn = new FileInputStream("en-token.bin");
        TokenizerModel model = new TokenizerModel(modelIn);

        // Create a new tokenizer
        Tokenizer tokenizer = new TokenizerME(model);

        // Analyze the keywords from the student's diploma
        String diploma = "Diploma in Computer Science with a specialization in Web Development";
        String[] tokens = tokenizer.tokenize(diploma);

        // Display the separated keywords
        for (String token : tokens)
            log.info(token);
        //log takone
        log.info("project was successfully added !");
        return projectRepository.save(project);
    }
*/
    @Override
    public List<Project> getAllProject() {
        log.info("list of projects");
        return projectRepository.findAll();}


    @Override
    public String deleteProject(Long idProject) {
        Project existingProject = projectRepository.findById(idProject).orElse(null);
        if(existingProject!=null) {
            projectRepository.delete(existingProject);
            log.info("project deleted");
            return "project deleted ";
        }
        return " this project is not existing";

    }


    @Override
    public Project updateProject(Long idProject, ProjectDTO projectDto) {
        Project project = projectRepository.findById(idProject).orElse(null);
        if (project != null) {
            projectMapper.updateProjectFromDto(projectDto, project);
            projectRepository.save(project);
            log.info("project was successfully updated !");
        }
        log.info("project not found !");
        return  project;

    }


}
