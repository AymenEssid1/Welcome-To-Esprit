package tn.esprit.springfever.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import tn.esprit.springfever.entities.Message;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IMessageService {
    public Message addMessage(String message, Long rec, HttpServletRequest request) throws JsonProcessingException;
    public Message updateMessage(Long id,String post, HttpServletRequest request) throws JsonProcessingException;
    public String deleteMessage(Long message, HttpServletRequest request) throws JsonProcessingException;
    public boolean convoExists(String id);
    public List<Message> getMessageByUser(HttpServletRequest request) throws JsonProcessingException;

    public String deleteMessageByUser(HttpServletRequest request) throws JsonProcessingException;
    public String deleteConversation(String id, HttpServletRequest request) throws JsonProcessingException;

    public List<Message> getMsgsByConvo(String id);

    public List<String> getConvsByUser(HttpServletRequest request) throws JsonProcessingException;
}
