package tn.esprit.springfever.services.interfaces;

import tn.esprit.springfever.entities.Message;

import java.util.List;

public interface IMessageService {
    public Message addMessage(Message msg);
    public Message updateMessage(Long id,Message post);
    public String deleteMessage(Long message);
    public boolean convoExists(String id);
    public List<Message> getAllMessages();
    public List<Message> getMessageByUser(int user);

    public String deleteMessageByUser(int id);
    public String deleteConversation(String id, int user);

    public List<Message> getMsgsByConvo(String id);

    public List<String> getConvsByUser(int user);
}
