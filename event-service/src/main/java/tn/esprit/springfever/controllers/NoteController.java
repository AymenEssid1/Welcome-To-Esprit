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

import java.io.IOException;
import java.util.List;


@RequestMapping("/Note")
@RestController(value = "/note")
@Api( tags = "note")



public class NoteController {


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



}
