package tn.esprit.springfever.controllers;

import com.twilio.Twilio;
import com.twilio.rest.accounts.v1.AuthTokenPromotion;
import com.twilio.rest.api.v2010.account.Message;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.DTO.NoteDTO;
//import tn.esprit.springfever.Security.jwt.JwtUtils;
import tn.esprit.springfever.Services.Interfaces.IServiceNote;
import tn.esprit.springfever.Services.Interfaces.IServiceProject;
import tn.esprit.springfever.Services.Interfaces.IServiceTeams;
import tn.esprit.springfever.entities.Note;
import tn.esprit.springfever.entities.Project;
import tn.esprit.springfever.entities.Teams;
import tn.esprit.springfever.repositories.NoteRepository;

import tn.esprit.springfever.configuration.SMS_service;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequestMapping("/Note")
@RestController(value = "/note")
@Api( tags = "note")


@Validated
public class NoteController {

    @Autowired
    NoteRepository noteRepository;
    @Autowired
    IServiceNote iServiceNote;
    @Autowired
    IServiceProject iServiceProject;

    @Autowired
    SMS_service sms_service;

    @Autowired
    IServiceTeams iServiceTeams;

    /*
    @Autowired
    private JwtUtils jwtUtils;
*/
    /*********  add note  ***********/
    @ApiOperation(value = "This method is used to add note")

    @PostMapping("/add")
    @ResponseBody
    public Note addNote(@Valid @RequestBody Note note) throws IOException {
        //iServiceNote.sendSmsvalide();
        return  iServiceNote.addNote(note);}


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

/*
    @PutMapping("assignNoteToProject/{idNote}/{idProject}")
    public String assignNoteToProject(@PathVariable("idNote") Long idNote, @PathVariable("idProject") Long idProject) {
        return iServiceNote.assignNoteToProject(idNote, idProject);
    }

*/

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



    @PostMapping("/sentiment")
    public ResponseEntity<String> analyzeSentiment(@RequestBody String text) {
        String sentiment = iServiceNote.analyzeSentiment(text);
        return ResponseEntity.ok(sentiment);
    }

    @PutMapping("/{id}/comment")
    public String sentFeedback(@PathVariable("id") Long idNote, @RequestBody String comment) {
        return iServiceNote.sentFeedback(idNote,comment);

    }





    @GetMapping("statistiques/notes")
    public ResponseEntity<Map<String, Float>> getNotesStatistics() {

        iServiceNote.sendSmsvalide();

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


    @PostMapping("/send-sms")
    public ResponseEntity<?> sendSMSToUserWithMaxProjectNote() throws IOException {
        // get the user with the max project note
        Teams teams = iServiceTeams.getTeamsWithMaxProjectNote();

        if (teams == null) {
            // return an error response if there are no users
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No teams found");
        }

        // get the Twilio client
        AuthTokenPromotion twilioConfig = null;
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());

        // create a message to send
        String message = "Hello "   + ", you have the highest project note!";

        // send the message
        Message sms = Message.creator(
                        new com.twilio.type.PhoneNumber("+21694602836"),  // recipient phone number
                        new com.twilio.type.PhoneNumber("+16076899788"),  // Twilio phone number
                        message)
                .create();

        // save the message in the database
        iServiceNote.addNote(new Note(
                "SMS to user " ,  // note title
                message,  // note content
                new Date()  // note date
        ));

        // return a success response
        return ResponseEntity.ok("SMS sent successfully");
    }

/*
    @GetMapping("/with-project-and-team-info")
    public List<NoteDTO> getNotesWithProjectAndTeamInfo() {
        return iServiceNote.getNotesWithProjectAndTeamInfo();
    }*/

}
