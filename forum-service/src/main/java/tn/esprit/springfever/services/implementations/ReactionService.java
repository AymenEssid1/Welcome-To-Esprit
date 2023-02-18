package tn.esprit.springfever.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.springfever.entities.Message;
import tn.esprit.springfever.entities.PostMedia;
import tn.esprit.springfever.entities.Reaction;
import tn.esprit.springfever.repositories.FileSystemRepository;
import tn.esprit.springfever.repositories.PostMediaRepository;
import tn.esprit.springfever.repositories.ReactionRepository;
import tn.esprit.springfever.services.interfaces.IReactionService;

import java.util.List;

@Service
@Slf4j
public class ReactionService implements IReactionService {
    @Autowired
    FileSystemRepository fileSystemRepository;
    @Autowired
    ReactionRepository repo;

    public Reaction save(byte[] bytes, String imageName, String label) throws Exception {
        String location = fileSystemRepository.save(bytes, imageName);
        return repo.save(new Reaction(imageName, location, label));
    }
    public FileSystemResource find(Long imageId) {
        Reaction image = repo.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return fileSystemRepository.findInFileSystem(image.getLocation());
    }
}
