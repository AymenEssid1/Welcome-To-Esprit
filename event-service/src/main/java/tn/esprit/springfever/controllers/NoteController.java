package tn.esprit.springfever.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.DTO.NoteDTO;
//import tn.esprit.springfever.Security.jwt.JwtUtils;
import tn.esprit.springfever.Services.Interfaces.IServiceNote;
import tn.esprit.springfever.entities.Note;
import tn.esprit.springfever.entities.Project;
import tn.esprit.springfever.entities.Teams;
import tn.esprit.springfever.repositories.NoteRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequestMapping("/Note")
@RestController(value = "/note")
@Api( tags = "note")



public class NoteController {

    @Autowired
    NoteRepository noteRepository;
    @Autowired
    IServiceNote iServiceNote;
    /*
    @Autowired
    private JwtUtils jwtUtils;
*/
    /*********  add note  ***********/
    @ApiOperation(value = "This method is used to add note")

    @PostMapping("/add")
    @ResponseBody
    public Note addNote(@RequestBody Note note) throws IOException {return  iServiceNote.addNote(note);}


    /*********  update note  ***********/
    @PutMapping("/update/{idNote}")
    @ResponseBody
    public Note updateNote(@PathVariable Long idNote, @RequestBody NoteDTO noteDTO )  {return  iServiceNote.updateNote(idNote , noteDTO);}


    /*********  get all note   ***********/
    @GetMapping("/getAllNote")
    @ResponseBody
    public List<Note> getAllNote()  {return  iServiceNote.getAllNote();}


    /*********  delete note  ***********/
    @DeleteMapping("/deleteNote/{idNote}")
    @ResponseBody
    public  boolean deleteNote(@PathVariable Long idNote)  {return  iServiceNote.deleteNote(idNote);}


    @PutMapping("assignNoteToProject/{idNote}/{idProject}")
    public String assignNoteToProject(@PathVariable Long idNote, @PathVariable Long idProject) {
        return iServiceNote.assignNoteToProject(idNote, idProject);
    }

/*
    @GetMapping("/{id}/sentiment")
    public ResponseEntity<String> getNoteSentiment(@PathVariable Long idNote) {


        String sentiment = iServiceNote.getSentimentAnalysis(idNote);
        if (sentiment == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(sentiment);
        }
    }
*/


    @GetMapping("statistiques/notes")
    public ResponseEntity<Map<String, Float>> getNotesStatistics() {
        Map<String, Float> statistics = new HashMap<>();

        // Calculate the average of each note type
        float softskillsNoteAverage = noteRepository.getAverageSoftskillsNote();
        float presentationNoteAverage = noteRepository.getAveragePresentationNote();
        float consistencyNoteAverage = noteRepository.getAverageConsistencyNote();
        float originalityNoteAverage = noteRepository.getAverageOriginalityNote();
        float contentNoteAverage = noteRepository.getAverageContentNote();
        float relevanceNoteAverage = noteRepository.getAverageRelevanceNote();

        // Add the averages to the map
        statistics.put("softskillsNoteAverage", softskillsNoteAverage);
        statistics.put("presentationNoteAverage", presentationNoteAverage);
        statistics.put("consistencyNoteAverage", consistencyNoteAverage);
        statistics.put("originalityNoteAverage", originalityNoteAverage);
        statistics.put("contentNoteAverage", contentNoteAverage);
        statistics.put("relevanceNoteAverage", relevanceNoteAverage);

        return ResponseEntity.ok(statistics);
    }

}
