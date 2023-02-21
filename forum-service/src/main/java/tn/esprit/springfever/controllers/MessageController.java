package tn.esprit.springfever.controllers;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.entities.Message;
import tn.esprit.springfever.services.interfaces.IMessageService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/msg")
@Api(tags = "Messages Module")
@Tag(name = "Messages Module")
@CrossOrigin
public class MessageController {
    @Autowired
    private IMessageService service;

    @PostMapping(value="/")
    @ResponseBody
    public ResponseEntity<Message> addMessage(Message m){
        m.setTimestamps(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addMessage(m));
    }

    @PutMapping(value="/")
    @ResponseBody
    public ResponseEntity<Message> putMessage(Long id , Message m){

        return ResponseEntity.ok().body(service.updateMessage(id, m));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long id){
        return ResponseEntity.ok().body(service.deleteMessage(id));
    }
    @DeleteMapping("/convo/{id}/{user}")
    public ResponseEntity<String> deleteConversation(@PathVariable String id, @PathVariable int user){
        return ResponseEntity.ok().body(service.deleteConversation(id,user));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteMessageByUser(@PathVariable int id){
        return ResponseEntity.ok().body(service.deleteMessageByUser(id));
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable int id){
        return ResponseEntity.ok().body(service.getMessageByUser(id));
    }

    @GetMapping(value="/convo/{id}")
    public ResponseEntity<List<Message>> getMessagesByConvo(@PathVariable String id){
        return ResponseEntity.ok().body(service.getMsgsByConvo(id));
    }

    @GetMapping(value = "exists/{id}")
    public ResponseEntity<?> convoExists(String id){
        if (service.convoExists(id)){
            return ResponseEntity.ok().body(true);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "user/convo/{id}")
    public ResponseEntity<List<String>> getConvsByUser(int id){
        return ResponseEntity.ok().body(service.getConvsByUser(id));
    }


}
