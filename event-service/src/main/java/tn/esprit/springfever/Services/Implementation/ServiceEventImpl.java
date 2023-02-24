package tn.esprit.springfever.Services.Implementation;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.DTO.EventDTO;
import tn.esprit.springfever.DTO.ProjectDTO;
import tn.esprit.springfever.DTO.TeamsDTO;
import tn.esprit.springfever.Services.Interfaces.EventMapper;
import tn.esprit.springfever.Services.Interfaces.IServiceEvent;
import tn.esprit.springfever.entities.Event;
import tn.esprit.springfever.entities.Project;
import tn.esprit.springfever.entities.Teams;
import tn.esprit.springfever.entities.User;
import tn.esprit.springfever.repositories.EventRepository;
import tn.esprit.springfever.repositories.TeamsRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class ServiceEventImpl implements IServiceEvent{

    @Autowired
    EventRepository eventRepository ;

    @Autowired
    private TeamsRepository teamsRepository;
    @Autowired
    EventMapper eventMapper;


    @Override
    public Event addEvent(Event event) {
        log.info("event was successfully added !");
        return eventRepository.save(event);
    }
/*
    @Override
    public Event addEvent(Event event) throws IOException {

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
        log.info("event was successfully added !");
        return eventRepository.save(event);
    }



 */
    @Override
    public List<Event> getAllEvent() {return eventRepository.findAll();}


    @Override
    public boolean deleteEvent(Long idEvent) {
        Event existingEvent = eventRepository.findById(idEvent).orElse(null);
        if(existingEvent!=null) {
            eventRepository.delete(existingEvent);
            log.info("event deleted");
            return true ;
        }
        log.info(" this event is not existing");
        return false;
    }


    @Override
    public Event updateEvent(Long idEvent, EventDTO eventDTO) {
        Event event = eventRepository.findById(idEvent).orElse(null);
        if (event != null) {
            eventMapper.updateEventFromDto(eventDTO, event);
            eventRepository.save(event);
            log.info("event was successfully updated !");
        }
        log.info("event not found !");
        return  event;

    }


    @Override
    public String assignTeamsToEvent(Long idEvent, Long idTeam) {

            Teams teams =teamsRepository.findById(idTeam).orElse(null);
            Event event = eventRepository.findById(idEvent).orElse(null);
            if(teams!=null && event!=null){
                event.setTeams(teams);
                eventRepository.save(event);
                return "teams is Affeced To event";
            }
            return "teams Or event Are not found";


            /*         affect teams to event where event=app0
            if (teams != null && event != null) {
            if ("APP0".equals(event.getTypeEvent())) { // Check if typeEvent is equal to "APP0"
                event.setTeams(teams);
                eventRepository.save(event);
                return "Teams is assigned to event with id " + idEvent;
            } else {
                return "Event with id " + idEvent + " is not of type APP0";
            }
        } else {
            return "Teams or Event not found";
        }
            */

        }





}
