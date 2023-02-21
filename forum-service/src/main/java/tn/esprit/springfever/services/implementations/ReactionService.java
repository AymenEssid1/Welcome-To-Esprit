package tn.esprit.springfever.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.springfever.entities.AdMedia;
import tn.esprit.springfever.entities.Message;
import tn.esprit.springfever.entities.PostMedia;
import tn.esprit.springfever.entities.Reaction;
import tn.esprit.springfever.repositories.AdMediaRepository;
import tn.esprit.springfever.repositories.FileSystemRepository;
import tn.esprit.springfever.repositories.PostMediaRepository;
import tn.esprit.springfever.repositories.ReactionRepository;
import tn.esprit.springfever.services.interfaces.IReactionService;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class ReactionService implements IReactionService {
    @Autowired
    FileSystemRepository fileSystemRepository;
    @Autowired
    ReactionRepository repo;
    @Override
    public Reaction save(MultipartFile file, String name) throws Exception {
        String location = fileSystemRepository.save(file);
        return repo.save(new Reaction(name, location, file.getBytes()));
    }

    @Override
    public FileSystemResource find(Long imageId) {
        Reaction image = repo.findById(Long.valueOf(imageId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return fileSystemRepository.findInFileSystem(image.getLocation());
    }

    @Override
    public Reaction getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public List<Reaction> getAll() {
        return repo.findAll();
    }

    @Override
    public Reaction updateReaction(Long id, String name,MultipartFile file) throws Exception {
        Reaction r = repo.findById(id).orElse(null);
        if (name!=null && !r.getName().equals(name)){
            r.setName(name);
        }
        if (file != null){
            if(file.getBytes().length != r.getContent().length){
                fileSystemRepository.deletefile(r.getLocation());
                String location = fileSystemRepository.save(file);
                r.setLocation(location);
                r.setContent(file.getBytes());
            }
        }
        return repo.save(r);
    }

    @Override
    public void delete(Long id) {
        Reaction image = repo.findById(Long.valueOf(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        fileSystemRepository.deletefile(image.getLocation());
        repo.delete(image);
    }
}
