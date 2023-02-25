package tn.esprit.springfever.Services.Interfaces;
import tn.esprit.springfever.DTO.NoteDTO;
import tn.esprit.springfever.entities.Note;
import tn.esprit.springfever.entities.Project;
import tn.esprit.springfever.entities.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
public interface IServiceNote {
    public Note addNote(Note note) throws IOException;
    public  List<Note> getAllNote() ;
    public  boolean deleteNote(Long idNote) ;
    public Note updateNote (Long idNote , NoteDTO noteDTO) ;
    public String assignNoteToProject(Long idNote, Long idProject);
}
