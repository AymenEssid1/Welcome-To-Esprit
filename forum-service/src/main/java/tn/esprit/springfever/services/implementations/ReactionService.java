package tn.esprit.springfever.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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

    @Override
    public FileSystemResource find(Long imageId) {
        Reaction image = repo.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return fileSystemRepository.findInFileSystem(image.getLocation());
    }
    @Override
    public List<Reaction> getAll() {
        return repo.findAll();
    }
    @Override
    public Reaction getById(Long id) {
        return repo.findById(id).orElse(null);
    }
    @Override
    public String deleteReaction(Long id) {
        repo.delete(repo.findById(id).orElse(null));
        return "Reaction Deleted!";
    }

    @Override
    public Reaction updateReaction(Long id, Reaction r) {
        Reaction reaction = repo.findById(id).orElse(null);
        if (reaction!=null){
            reaction.setName(r.getName());
            if (!r.getLocation().equals(reaction.getLocation())){
                fileSystemRepository.deletefile(reaction.getLocation());
                reaction.setLocation(r.getLocation());
            }

            return repo.save(reaction);
        }else{
            return null;
        }
    }

    @Override
    public Reaction addReaction(Reaction reaction) {
        return null;
    }
}
