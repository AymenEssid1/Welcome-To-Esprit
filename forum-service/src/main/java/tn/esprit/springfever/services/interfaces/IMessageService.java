package tn.esprit.springfever.services.interfaces;

import tn.esprit.springfever.entities.Message;

import java.util.List;

public interface IMessageService {
    public Message addMessage(Message msg);
    public Message updateMessage(int id,Message post);
    public String deleteMessage(int message);
    public List<Message> getAllMessages();
    public List<Message> getPostsByUser(int user);
}
