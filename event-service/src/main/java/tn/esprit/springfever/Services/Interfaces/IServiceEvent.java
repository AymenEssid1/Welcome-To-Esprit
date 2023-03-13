package tn.esprit.springfever.Services.Interfaces;
import tn.esprit.springfever.DTO.EventDTO;
import tn.esprit.springfever.entities.Event;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
public interface IServiceEvent {
    public Event addEvent(Event event) throws IOException;
    public  List<Event> getAllEvent() ;
    public  boolean deleteEvent(Long idEvent) ;
    public Event updateEvent (Long idEvent , EventDTO eventDTO) ;
    public String assignTeamsToEvent(Long idEvent, Long idTeam);
}
