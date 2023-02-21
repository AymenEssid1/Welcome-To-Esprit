package tn.esprit.springfever.services.interfaces;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.PostMedia;

import java.io.IOException;
import java.util.List;

public interface IPostMediaService {
    public PostMedia save(MultipartFile file, Post post) throws Exception ;
    public FileSystemResource find(Long imageId) ;
    public void delete(Long id);
}
