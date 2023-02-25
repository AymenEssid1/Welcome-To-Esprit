package tn.esprit.springfever.Services.Implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.DTO.NoteDTO;
import tn.esprit.springfever.Services.Interfaces.NoteMapper;
import tn.esprit.springfever.Services.Interfaces.IServiceNote;
import tn.esprit.springfever.entities.*;
import tn.esprit.springfever.repositories.NoteRepository;
import tn.esprit.springfever.repositories.ProjectRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@Slf4j

public class ServiceNoteImpl implements IServiceNote {
    @Autowired
    NoteRepository noteRepository ;
    @Autowired
    ProjectRepository projectRepository ;
    @Autowired
    NoteMapper noteMapper;


    @Override
    public Note addNote(Note note) {
        log.info("event was successfully added !");
        return noteRepository.save(note);
    }
    /*
    @Override
    public Note addNote(Note note) throws IOException {

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
        log.info("note was successfully added !");
        return noteRepository.save(note);
    }
*/
    @Override
    public List<Note> getAllNote() {return noteRepository.findAll();}


    @Override
    public boolean deleteNote(Long idNote) {
        Note existingNote = noteRepository.findById(idNote).orElse(null);
        if(existingNote!=null) {
            noteRepository.delete(existingNote);
            log.info("note deleted");
            return true ;
        }
        log.info(" this note is not existing");
        return false;
    }


    @Override
    public Note updateNote(Long idNote, NoteDTO noteDto) {
        Note note = noteRepository.findById(idNote).orElse(null);
        if (note != null) {
            noteMapper.updateNoteFromDto(noteDto, note);
            noteRepository.save(note);
            log.info("note was successfully updated !");
        }
        log.info("note not found !");
        return  note;

    }


    /*
    public Note addNoteToProject(Long projectId, Note note) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project with id " + projectId + " not found"));
        note.setProject(project);

        Double projectNote = (note.getSoftskillsNote() + note.getHardskillsNote() + note.getPresentationNote() +
                note.getConsistencyNote() + note.getOriginalityNote() + note.getContentNote() +
                note.getRelevanceNote()) / 7.0;

        note.setProjectNote(projectNote);

        noteRepository.save(note);
        return note;
    }*/

@Override
    public String assignNoteToProject(Long idNote, Long idProject) {
        Note note = noteRepository.findById(idNote).orElse(null);
        Project project = projectRepository.findById(idProject).orElse(null);

    if (note != null && project != null) {
        // Calculate the project note
        float projectNote = (note.getSoftskillsNote() + note.getHardskillsNote() + note.getPresentationNote() +
                note.getConsistencyNote() + note.getOriginalityNote() + note.getContentNote() + note.getRelevanceNote()) / 7;
        note.setProjectNote(projectNote);

        // Assign project note to project

        Note notes = project.getNote();
        project.setNote(notes);

    }
       // note.setProject( project);           hedhy lzm na7eha maach comm bch njm nsavi f project ama feha erreur !!!
        noteRepository.save(note);
        projectRepository.save(project);
        return "note is Affeced To project";

    }

}
