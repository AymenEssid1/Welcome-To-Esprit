package tn.esprit.springfever.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.entities.Message;
import tn.esprit.springfever.services.interfaces.IMessageService;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<Message> addMessage(String message, Long rec, HttpServletRequest request) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addMessage(message,rec,request));
    }

    @PutMapping(value="/")
    @ResponseBody
    public ResponseEntity<Message> putMessage(Long id,String post, HttpServletRequest request) throws JsonProcessingException {

        return ResponseEntity.ok().body(service.updateMessage(id, post, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long id, HttpServletRequest request) throws JsonProcessingException {
        return ResponseEntity.ok().body(service.deleteMessage(id, request));
    }
    @DeleteMapping("/convo/{id}")
    public ResponseEntity<String> deleteConversation(@PathVariable String id, HttpServletRequest request) throws JsonProcessingException {
        return ResponseEntity.ok().body(service.deleteConversation(id,request));
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteMessageByUser(HttpServletRequest request) throws JsonProcessingException {
        return ResponseEntity.ok().body(service.deleteMessageByUser(request));
    }

    @GetMapping(value="/")
    public ResponseEntity<List<Message>> getMessages(HttpServletRequest request) throws JsonProcessingException {
        return ResponseEntity.ok().body(service.getMessageByUser(request));
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
    public ResponseEntity<List<String>> getConvsByUser(HttpServletRequest request) throws JsonProcessingException {
        return ResponseEntity.ok().body(service.getConvsByUser(request));
    }


}
