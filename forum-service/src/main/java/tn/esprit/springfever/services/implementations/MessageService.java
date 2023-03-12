package tn.esprit.springfever.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.dto.LikesDTO;
import tn.esprit.springfever.dto.MessageDTO;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.Likes;
import tn.esprit.springfever.entities.Message;
import tn.esprit.springfever.repositories.MessageRepository;
import tn.esprit.springfever.services.ConvoGenerator;
import tn.esprit.springfever.services.interfaces.IMessageService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MessageService implements IMessageService {
    @Autowired
    private MessageRepository repo;
    @Autowired
    private UserService userService;

    @Override
    public MessageDTO addMessage(String message, Long rec, HttpServletRequest request) throws JsonProcessingException {
        if (request != null && request.getHeader(HttpHeaders.AUTHORIZATION) != null) {
            Long sender = userService.getUserDetailsFromToken(request.getHeader(HttpHeaders.AUTHORIZATION)).getId();
            List<Message> list1 = repo.findBySenderAndReceiver(sender, rec);
            List<Message> list2 = repo.findBySenderAndReceiver(rec, sender);
            Message msg = new Message();
            msg.setSender(sender);
            msg.setReceiver(rec);
            msg.setTimestamps(LocalDateTime.now());
            msg.setMsg(message);
            if (list1.isEmpty()) {
                if (list2.isEmpty()) {
                    msg.setConvId(new ConvoGenerator().generate(null, null).toString());
                } else {
                    msg.setConvId(list2.get(0).getConvId());
                }
            } else {
                msg.setConvId(list1.get(0).getConvId());
            }
            Message message1= repo.save(msg);
            return  convertToLikesDTO(message1);
        } else {
            return null;
        }

    }

    @Override
    @CachePut("msg")
    public MessageDTO updateMessage(Long id, String msg, HttpServletRequest request) throws JsonProcessingException {
        if (request != null && request.getHeader(HttpHeaders.AUTHORIZATION) != null) {
            Long sender = userService.getUserDetailsFromToken(request.getHeader(HttpHeaders.AUTHORIZATION)).getId();
            Message p = repo.findById(Long.valueOf(id)).orElse(null);
            if (p != null) {
                p.setMsg(msg);
                p.setTimestamps(LocalDateTime.now());
                repo.save(p);
            }
            return convertToLikesDTO(p);
        } else {
            return null;
        }
    }

    @Override
    @CacheEvict("msg")
    public String deleteMessage(Long message, HttpServletRequest request) throws JsonProcessingException {
        if (request != null && request.getHeader(HttpHeaders.AUTHORIZATION) != null) {
            Long sender = userService.getUserDetailsFromToken(request.getHeader(HttpHeaders.AUTHORIZATION)).getId();
            Message p = repo.findById(Long.valueOf(message)).orElse(null);
            if (p != null) {
                if (p.getSender() == sender || p.getReceiver() == sender) {
                    repo.delete(p);
                    return "Message was successfully deleted !";
                } else {
                    return "You don't have the right to delete this message";
                }
            }
            return "Not Found ! ";
        } else {
            return "You have to login!";
        }
    }

    @Override
    @Cacheable("msg")
    public List<MessageDTO> getMessageByUser(HttpServletRequest request) throws JsonProcessingException {
        if (request != null && request.getHeader(HttpHeaders.AUTHORIZATION) != null) {
            Long sender = userService.getUserDetailsFromToken(request.getHeader(HttpHeaders.AUTHORIZATION)).getId();
            List<Message> messages = repo.findBySenderOrReceiver(sender, sender);
            return convertToLikesDTOS(messages);
        } else {
            return null;
        }
    }

    @Override
    @CacheEvict("msg")
    public String deleteMessageByUser(HttpServletRequest request) throws JsonProcessingException {
        if (request != null && request.getHeader(HttpHeaders.AUTHORIZATION) != null) {
            Long sender = userService.getUserDetailsFromToken(request.getHeader(HttpHeaders.AUTHORIZATION)).getId();
            List<Message> msgs = repo.findBySenderOrReceiver(sender, sender);
            deleteSequence(msgs, sender);
            return "deleted succefully!";
        } else {
            return null;
        }


    }

    @Override
    @CacheEvict("msg")
    public String deleteConversation(String id, HttpServletRequest request) throws JsonProcessingException {
        if (request != null && request.getHeader(HttpHeaders.AUTHORIZATION) != null) {
            Long sender = userService.getUserDetailsFromToken(request.getHeader(HttpHeaders.AUTHORIZATION)).getId();
            List<Message> msgs = repo.findByConvId(id);
            log.info(String.valueOf(msgs.size()));
            deleteSequence(msgs, sender);
            return "deleted succefully!";
        } else {
            return null;
        }
    }

    @Override
    @Cacheable("msg")
    public List<MessageDTO> getMsgsByConvo(String id) throws JsonProcessingException {
        return convertToLikesDTOS(repo.findByConvId(id));
    }

    @Override
    @Cacheable("msg")
    public List<String> getConvsByUser(HttpServletRequest request) throws JsonProcessingException {
        if (request != null && request.getHeader(HttpHeaders.AUTHORIZATION) != null) {
            Long sender = userService.getUserDetailsFromToken(request.getHeader(HttpHeaders.AUTHORIZATION)).getId();
            return repo.findDistinctConversationsBySenderOrReceiver(sender, sender);
        }else{
            return null;
        }
    }

    @Override
    public boolean convoExists(String id) {
        return !repo.findByConvId(id).isEmpty();
    }

    public void deleteSequence(List<Message> msgs, Long user) {
        for (Message m : msgs) {
            if (m.getReceiver() == user) {
                m.setReceiver(0L);
            } else {
                m.setSender(0L);
            }
            if (m.getReceiver() == 0 && m.getReceiver() == 0) {
                repo.delete(m);
            } else {
                repo.save(m);
            }
        }
    }

    public MessageDTO convertToLikesDTO(Message message) throws JsonProcessingException {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setMsg(message.getMsg());
        messageDTO.setTimestamps(message.getTimestamps());
        messageDTO.setConvId(message.getConvId());
        messageDTO.setSender(userService.getUserDetailsFromId(message.getSender()));
        messageDTO.setReceiver(userService.getUserDetailsFromId(message.getReceiver()));
        return messageDTO;
    }

    public List<MessageDTO> convertToLikesDTOS(List<Message> messages) throws JsonProcessingException {
        List<MessageDTO> messageDTOS = new ArrayList<>();
        List<Long> sendersIds = new ArrayList<>();
        List<Long> receiversIds = new ArrayList<>();
        for (Message message : messages) {
            sendersIds.add(message.getSender());
            receiversIds.add(message.getReceiver());
        }
        List<UserDTO> senders = userService.getUserDetailsFromIds(sendersIds);
        List<UserDTO> receivers = userService.getUserDetailsFromIds(receiversIds);
        for (Message message : messages){
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setId(message.getId());
            messageDTO.setMsg(message.getMsg());
            messageDTO.setTimestamps(message.getTimestamps());
            messageDTO.setConvId(message.getConvId());
            messageDTO.setSender(senders.get(messages.indexOf(message)));
            messageDTO.setReceiver(receivers.get(messages.indexOf(message)));
            messageDTOS.add(messageDTO);
        }
        return messageDTOS;
    }

}
