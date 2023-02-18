package tn.esprit.springfever.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.entities.Message;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.repositories.MessageRepository;
import tn.esprit.springfever.services.interfaces.IMessageService;

import java.util.List;

@Service
@Slf4j
public class MessageService implements IMessageService {
    @Autowired
    private MessageRepository repo;
    @Override
    public Message addMessage(Message msg) {
        return repo.save(msg);
    }

    @Override
    public Message updateMessage(int id, Message msg) {
        Message p = repo.findById(Long.valueOf(id)).orElse(null) ;
        if(p!=null) {
            msg.setId(p.getId());
            repo.save(msg);
        }
        return p;
    }

    @Override
    public String deleteMessage(int message) {
        Message p = repo.findById(Long.valueOf(message)).orElse(null) ;
        if(p!=null) {
            repo.delete(p);
            return "Post was successfully deleted !" ;
        }
        return "Not Found ! ";
    }

    @Override
    public List<Message> getAllMessages() {
        return repo.findAll();
    }

    @Override
    public List<Message> getPostsByUser(int user) {
        return repo.findBySenderAndReceiver(user);
    }
}
