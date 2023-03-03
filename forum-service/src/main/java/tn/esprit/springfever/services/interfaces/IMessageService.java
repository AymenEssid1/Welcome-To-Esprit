package tn.esprit.springfever.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import tn.esprit.springfever.dto.MessageDTO;
import tn.esprit.springfever.entities.Message;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IMessageService {
    public MessageDTO addMessage(String message, Long rec, HttpServletRequest request) throws JsonProcessingException;
    public MessageDTO updateMessage(Long id,String post, HttpServletRequest request) throws JsonProcessingException;
    public String deleteMessage(Long message, HttpServletRequest request) throws JsonProcessingException;
    public boolean convoExists(String id);
    public List<MessageDTO> getMessageByUser(HttpServletRequest request) throws JsonProcessingException;

    public String deleteMessageByUser(HttpServletRequest request) throws JsonProcessingException;
    public String deleteConversation(String id, HttpServletRequest request) throws JsonProcessingException;

    public List<MessageDTO> getMsgsByConvo(String id) throws JsonProcessingException;

    public List<String> getConvsByUser(HttpServletRequest request) throws JsonProcessingException;
}
