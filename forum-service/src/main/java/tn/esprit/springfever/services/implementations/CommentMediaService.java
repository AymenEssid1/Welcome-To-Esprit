package tn.esprit.springfever.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.springfever.entities.CommentMedia;
import tn.esprit.springfever.entities.PostMedia;
import tn.esprit.springfever.repositories.CommentMediaRepository;
import tn.esprit.springfever.repositories.FileSystemRepository;
import tn.esprit.springfever.repositories.PostMediaRepository;
import tn.esprit.springfever.services.interfaces.ICommentMediaService;

@Service
@Slf4j
public class CommentMediaService implements ICommentMediaService {
    @Autowired
    FileSystemRepository fileSystemRepository;
    @Autowired
    CommentMediaRepository repo;

    public CommentMedia save(byte[] bytes, String imageName) throws Exception {
        String location = fileSystemRepository.save(bytes, imageName);
        return repo.save(new CommentMedia(imageName, location));
    }
    public FileSystemResource find(Long imageId) {
        CommentMedia image = repo.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return fileSystemRepository.findInFileSystem(image.getLocation());
    }
}
