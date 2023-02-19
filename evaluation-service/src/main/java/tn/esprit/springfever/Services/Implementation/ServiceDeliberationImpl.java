package tn.esprit.springfever.Services.Implementation;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.entities.Deliberation;
import tn.esprit.springfever.entities.User;
import tn.esprit.springfever.repositories.DeliberationRepository;
import tn.esprit.springfever.repositories.UserRepository;
import tn.esprit.springfever.Services.Interfaces.IServiceDeliberation;

@Service
@Slf4j
public class ServiceDeliberationImpl implements IServiceDeliberation {

    @Autowired
    DeliberationRepository deliberationRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public Deliberation addDeliberation(Deliberation deliberation) {
        log.info("Deliberation was successfully added!");
        return deliberationRepository.save(deliberation);
    }

    @Override
    public List<Deliberation> getAllDeliberations() {
        log.info("List of deliberations : ");
        return deliberationRepository.findAll();
    }

    @Override
    public String deleteDeliberation(Long idDeliberation) {
        Deliberation existingDeliberation = deliberationRepository.findById(idDeliberation).orElse(null);
        if (existingDeliberation != null) {
            deliberationRepository.delete(existingDeliberation);
            log.info("Deliberation was successfully deleted!");
            return "Deliberation was successfully deleted!";
        }
        return "This Deliberation is not existing!";
    }

    @Override
    public Deliberation updateDeliberation(Long idDeliberation, Deliberation deliberation) {
        Deliberation existingDeliberation = deliberationRepository.findById(idDeliberation).orElse(null);
        if (existingDeliberation != null) {
            existingDeliberation.setResult(deliberation.getResult());

            deliberationRepository.save(existingDeliberation);
            log.info("Deliberation was successfully updated!");
        }
        log.info("This Deliberation is not existing!");
        return existingDeliberation;
    }

    @Override
    public Deliberation getDeliberationOfUser(String username) {
        User user = userRepository.findByUsername(username).get();
        if (user == null) {
            log.info("User with username: " + username + " does not exist!");
            return null;
        } else {
            Deliberation deliberation = user.getDeliberation();
            if (deliberation == null) {
                log.info("User with username: " + username + " has no Deliberation!");
            } else {
                log.info("Deliberation of User with username: " + username + " is found!");
            }
            return deliberation;
        }
    }

    @Override
    public Deliberation getDeliberationById(Long id) {
        return deliberationRepository.findById(id).orElse(null);
    }
}
