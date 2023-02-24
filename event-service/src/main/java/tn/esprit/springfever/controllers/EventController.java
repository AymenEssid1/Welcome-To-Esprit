package tn.esprit.springfever.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.DTO.EventDTO;
//import tn.esprit.springfever.Security.jwt.JwtUtils;
import tn.esprit.springfever.Services.Interfaces.IServiceEvent;
import tn.esprit.springfever.entities.Event;
import tn.esprit.springfever.entities.Teams;

import java.io.IOException;
import java.util.List;


@RequestMapping("/Event")
@RestController(value = "/event")
@Api( tags = "event")



public class EventController {


    @Autowired
    IServiceEvent iServiceEvent;
    /*
    @Autowired
    private JwtUtils jwtUtils;
*/
    /*********  add event  ***********/
    @ApiOperation(value = "This method is used to add event")

    @PostMapping("/add")
    @ResponseBody
    public Event addEvent(@RequestBody Event event) throws IOException {return  iServiceEvent.addEvent(event);}


    /*********  update event  ***********/
    @PutMapping("/update/{idEvent}")
    @ResponseBody
    public Event updateEvent(@PathVariable Long idEvent, @RequestBody EventDTO eventDTO )  {return  iServiceEvent.updateEvent(idEvent , eventDTO);}


    /*********  get all event   ***********/
    @GetMapping("/getAllEvents")
    @ResponseBody
    public List<Event> getAllEvent()  {return  iServiceEvent.getAllEvent();}


    /*********  delete teams  ***********/
    @DeleteMapping("/deleteEvent/{idEvent}")
    @ResponseBody
    public  boolean deleteEvent(@PathVariable Long idEvent)  {return  iServiceEvent.deleteEvent(idEvent);}


    @PutMapping("AssignTeamsToEvent/{idEvent}/{idTeam}")
    public String assignTeamsToEvent(@PathVariable Long idEvent, @PathVariable Long idTeam) {
        return iServiceEvent.assignTeamsToEvent(idEvent , idTeam);
    }


}
