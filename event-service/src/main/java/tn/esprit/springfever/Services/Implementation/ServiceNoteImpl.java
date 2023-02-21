package tn.esprit.springfever.Services.Implementation;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.DTO.NoteDTO;
import tn.esprit.springfever.DTO.ProjectDTO;
import tn.esprit.springfever.DTO.TeamsDTO;
import tn.esprit.springfever.Services.Interfaces.NoteMapper;
import tn.esprit.springfever.Services.Interfaces.IServiceNote;
import tn.esprit.springfever.entities.*;
import tn.esprit.springfever.repositories.NoteRepository;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j

public class ServiceNoteImpl implements IServiceNote {
    @Autowired
    NoteRepository noteRepository ;
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


}
