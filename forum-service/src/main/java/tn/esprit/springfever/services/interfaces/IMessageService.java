package tn.esprit.springfever.services.interfaces;

import tn.esprit.springfever.entities.Message;

import java.util.List;

public interface IMessageService {
    public Message addMessage(Message msg);
    public Message updateMessage(Long id,Message post);
    public String deleteMessage(Long message);
    public List<Message> getAllMessages();
    public List<Message> getPostsByUser(Long user);
}
