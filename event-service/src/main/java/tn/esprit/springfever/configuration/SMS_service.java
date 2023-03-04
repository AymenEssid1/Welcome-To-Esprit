package tn.esprit.springfever.configuration;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.Services.Interfaces.SmsService;
import tn.esprit.springfever.entities.Note;
import tn.esprit.springfever.repositories.NoteRepository;
import tn.esprit.springfever.repositories.ProjectRepository;
import tn.esprit.springfever.repositories.TeamsRepository;

import java.util.List;


@Service
@Slf4j
public class SMS_service {


    @Autowired
    NoteRepository noteRepository ;
    @Autowired
    TeamsRepository teamsRepository ;
    @Autowired
    ProjectRepository projectRepository ;
    @Autowired
    SmsService smsService ;

    public void sendSmsvalide(String s) {

        List<Note> notes = noteRepository.findAllByOrderByProjectNoteDesc();
        Note noteWithMaxProjectNote = notes.get(0);

        final String ACCOUNT_SID = "ACf5932234ee7104417d4aa27eeb82027d";
        final String AUTH_TOKEN = "a135f9e778d62e3198a72edd1a3f3496";
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        //Message msg = Message.creator(new PhoneNumber("+216"+s),new PhoneNumber("+16076899788"),("Congratulations! you are invited to the ceremonie and Your team has the highest project note :Note with max projectNote: ")).create();
        Message message = Message.creator(
                        new PhoneNumber("+21694602836"),
                        new PhoneNumber("+16076899788"),
                        "Congratulations! you are invited to the ceremonie and Your team has the highest project note :Note with max projectNote: " + noteWithMaxProjectNote.getProjectNote())
                .create();
    }
}

