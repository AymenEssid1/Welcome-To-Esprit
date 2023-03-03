package tn.esprit.springfever.Services.Implementation;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.springfever.DTO.ProjectDTO;
import tn.esprit.springfever.DTO.TeamsDTO;
import tn.esprit.springfever.Services.Interfaces.ProjectMapper;
import tn.esprit.springfever.Services.Interfaces.IServiceProject;
import tn.esprit.springfever.entities.Event;
import tn.esprit.springfever.entities.Project;
import tn.esprit.springfever.entities.Teams;
import tn.esprit.springfever.entities.User;
import tn.esprit.springfever.repositories.ProjectRepository;
import tn.esprit.springfever.repositories.rapportPDFRepository;

import javax.persistence.EntityNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ServiceProjectImpl implements IServiceProject{

    @Autowired
    ProjectRepository projectRepository ;
    @Autowired
    rapportPDFRepository rapportPdfRepository;

    @Autowired
    ProjectMapper projectMapper;

    @Override
    public Project addProject(Project project){
        log.info("project was successfully added !");
        return projectRepository.save(project);
    }

    public Project save(byte[] rapport, String imageName) throws Exception {
        return projectRepository.save(new Project(rapport));
    }


    public Project savef(byte[] rapport, String location_rapport) throws Exception {
        Path rapportFile = Paths.get("C:\\Users\\Nour\\Desktop\\" + new Date().getTime() + "-" + location_rapport);

        Files.write(rapportFile, rapport);

        String rapportLocation = rapportFile.toAbsolutePath().toString();

        return projectRepository.save(new Project( rapportLocation));
    }

    public FileSystemResource findrapport(Long Id_Job_Application) {

        Project project = projectRepository.findById(Id_Job_Application)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return rapportPDFRepository.findInFileSystem(project.getLocation_rapport());
    }

    public String extractTextFromPdf(Long id){
        String text = null;
        try {
            FileSystemResource fileSystemResource = findrapport(id);

            PdfReader reader = new PdfReader(fileSystemResource.getPath());

            int n = reader.getNumberOfPages();
            text = "";
            for (int i = 0; i < n; i++) {
                text += PdfTextExtractor.getTextFromPage(reader, i + 1).trim() + "\n";
            }
            reader.close();
            System.out.println(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
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

@Override
    public void uploadVideo(Long idProject,  MultipartFile video) throws IOException {
        // Find the project with the given ID
        Optional<Project> optionalProject = projectRepository.findById(idProject);
        if (optionalProject.isEmpty()) {
            throw new EntityNotFoundException("Project not found with ID " + idProject);
        }
        Project project = optionalProject.get();

        // Store the video data in the project entity
        project.setVideo(video.getBytes());
        projectRepository.save(project);



    }

}
