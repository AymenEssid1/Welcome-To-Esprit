package tn.esprit.springfever.services.interfaces;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.entities.AdMedia;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.PostMedia;
import tn.esprit.springfever.entities.Reaction;

import java.io.IOException;
import java.util.List;

public interface IReactionService {
    public Reaction save(MultipartFile file, String name) throws Exception ;
    public FileSystemResource find(Long imageId) ;
    public Reaction getById(Long id);
    public List<Reaction> getAll();
    public Reaction updateReaction(Long id, String name,MultipartFile file) throws Exception;
    public void delete(Long id);
}
