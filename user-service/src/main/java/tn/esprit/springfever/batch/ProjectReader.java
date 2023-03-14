package tn.esprit.springfever.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tn.esprit.springfever.Repositories.UserRepo;
import tn.esprit.springfever.entities.User;

import java.util.Iterator;
import java.util.List;


@Slf4j
@Component
public class ProjectReader implements ItemReader<ItemReader<User>> {
     @Autowired
    private UserRepo userRepository;

    private Iterator<User> userIterator;


    @Override
    public ItemReader<User> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        List<User> users = userRepository.findAll();
        Iterator<User> userIterator = users.iterator();

        return new IteratorItemReader<>(userIterator);
    }


}
