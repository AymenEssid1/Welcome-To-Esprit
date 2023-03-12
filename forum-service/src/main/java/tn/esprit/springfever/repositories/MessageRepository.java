package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import tn.esprit.springfever.entities.Message;


import java.util.List;

@EnableJpaRepositories
public interface MessageRepository extends JpaRepository<Message,Long> {
    public List<Message> findBySenderOrReceiver(Long sender, Long receiver);
    public List<Message> findBySenderAndReceiver(Long sender, Long receiver);
    public List<Message> findBySender(Long sender);
    public List<Message> findByConvId(String id);

    @Query(value =
            "SELECT DISTINCT m.convId " +
                    "FROM Message m " +
                    "WHERE m.sender = :sender OR m.receiver = :receiver")
    public List<String> findDistinctConversationsBySenderOrReceiver(@Param("sender") Long sender, @Param("receiver") Long receiver);


}