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
import tn.esprit.springfever.entities.*;
import tn.esprit.springfever.repositories.*;

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
    NoteRepository noteRepository ;
    @Autowired
    rapportPDFRepository rapportPdfRepository;

    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    FileSystemRepository  fileSystemRepository;
    @Autowired
    VideoRepository videoRepository;

    @Override
    public Project addProject(Project project){
        log.info("project was successfully added !");
        return projectRepository.save(project);
    }


@Override
    public List<Object[]> getAllProjectsAndNotes() {
        return projectRepository.findAllProjectsAndNotes();
    }

    public Project save(byte[] rapport, String imageName) throws Exception {
        return projectRepository.save(new Project(rapport));
    }


    public Project savef(byte[] rapport, String location_rapport,byte[] bytes, String videoName) throws Exception {
        Path rapportFile = Paths.get("C:\\Users\\Nour\\Desktop\\" + new Date().getTime() + "-" + location_rapport);
        Path video =Paths.get("C:\\Users\\Nour\\Desktop\\" + new Date().getTime() + "-" + videoName);
        Files.write(rapportFile, rapport);
        String location = fileSystemRepository.saveVideo(bytes, videoName);
         videoRepository.save(new Video(videoName, location));
         String videoLocation=video.toAbsolutePath().toString();

        String rapportLocation = rapportFile.toAbsolutePath().toString();

        return projectRepository.save(new Project( rapportLocation,videoLocation));
    }

    public FileSystemResource findrapport(Long idProject) {

        Project project = projectRepository.findById(idProject)
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


    @Override
    public String assignNoteToProject(Long idNote, Long idProject) {
        Note note = noteRepository.findById(idNote).orElse(null);
        Project project = projectRepository.findById(idProject).orElse(null);

        // Calculate the project note
        float projectNote = (note.getSoftskillsNote() + note.getHardskillsNote() + note.getPresentationNote() +
                note.getConsistencyNote() + note.getOriginalityNote() + note.getContentNote() + note.getRelevanceNote()) / 7;
        note.setProjectNote(projectNote);

        if (note != null && project != null) {


            // Assign project note to project

            Note notes = project.getNote();
            project.setNote(notes);
        }

        // note.setProject( project);           hedhy lzm na7eha maach comm bch njm nsavi f project ama feha erreur !!!
        noteRepository.save(note);
        project.setNote(note);
        projectRepository.save(project);
        return "note is Affeced To project";

    }

}
