package tn.esprit.springfever.services.interfaces;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.entities.AdMedia;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.Reaction;

import java.util.List;

public interface IReactionService {
    public List<Reaction> getAll();
    public Reaction getById(Long id);
    public String deleteReaction(Long id);
    public Reaction updateReaction(Long id, Reaction r);
    public Reaction addReaction(Reaction reaction);
    public FileSystemResource find(Long imageId);
}
