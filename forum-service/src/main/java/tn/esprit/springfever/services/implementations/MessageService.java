package tn.esprit.springfever.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.entities.Message;
import tn.esprit.springfever.repositories.MessageRepository;
import tn.esprit.springfever.services.ConvoGenerator;
import tn.esprit.springfever.services.interfaces.IMessageService;

import java.util.List;

@Service
@Slf4j
public class MessageService implements IMessageService {
    @Autowired
    private MessageRepository repo;

    @Override
    public Message addMessage(Message msg) {
        List<Message> list1 = repo.findBySenderAndReceiver(msg.getSender(), msg.getReceiver());
        List<Message> list2 = repo.findBySenderAndReceiver(msg.getReceiver(), msg.getSender());
        if (list1.isEmpty()) {
            log.info("list1 is empty");
            if (list2.isEmpty()) {
                log.info("list2 is empty");
                msg.setConvId(new ConvoGenerator().generate(null, null).toString());
            } else {
                msg.setConvId(list2.get(0).getConvId());
            }
        } else {
            msg.setConvId(list1.get(0).getConvId());
        }

        return repo.save(msg);
    }

    @Override
    @CachePut("msg")
    public Message updateMessage(Long id, Message msg) {
        Message p = repo.findById(Long.valueOf(id)).orElse(null);
        if (p != null) {
            msg.setId(p.getId());
            repo.save(msg);
        }
        return p;
    }

    @Override
    @CacheEvict("msg")
    public String deleteMessage(Long message) {
        Message p = repo.findById(Long.valueOf(message)).orElse(null);
        if (p != null) {
            repo.delete(p);
            return "Message was successfully deleted !";
        }
        return "Not Found ! ";
    }

    @Override
    @Cacheable("msg")
    public List<Message> getAllMessages() {
        return repo.findAll();
    }

    @Override
    @Cacheable("msg")
    public List<Message> getMessageByUser(int user) {
        return repo.findBySenderOrReceiver(user, user);
    }

    @Override
    @CacheEvict("msg")
    public String deleteMessageByUser(int id) {
        List<Message> msgs = repo.findBySenderOrReceiver(id, id);
        deleteSequence(msgs, id);
        return "deleted succefully!";
    }

    @Override
    @CacheEvict("msg")
    public String deleteConversation(String id, int user) {
        List<Message> msgs = repo.findByConvId(id);
        log.info(String.valueOf(msgs.size()));
        deleteSequence(msgs, user);
        return "deleted succefully!";
    }

    @Override
    @Cacheable("msg")
    public List<Message> getMsgsByConvo(String id) {
        return repo.findByConvId(id);
    }

    @Override
    @Cacheable("msg")
    public List<String> getConvsByUser(int user) {
        return repo.findDistinctConversationsBySenderOrReceiver(user, user);
    }

    @Override
    public boolean convoExists(String id) {
        return !repo.findByConvId(id).isEmpty();
    }

    public void deleteSequence(List<Message> msgs, int user) {
        for (Message m : msgs) {
            if (m.getReceiver() == user) {
                m.setReceiver(0);
            } else {
                m.setSender(0);
            }
            if (m.getReceiver() == 0 && m.getReceiver() == 0) {
                repo.delete(m);
            } else {
                repo.save(m);
            }
        }
    }
}
