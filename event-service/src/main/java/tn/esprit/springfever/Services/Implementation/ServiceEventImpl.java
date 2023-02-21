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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class ServiceEventImpl implements IServiceEvent{

    @Autowired
    EventRepository eventRepository ;
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


}
