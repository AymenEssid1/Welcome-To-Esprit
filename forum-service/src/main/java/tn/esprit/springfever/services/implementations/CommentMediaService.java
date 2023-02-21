package tn.esprit.springfever.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.springfever.entities.Comment;
import tn.esprit.springfever.entities.CommentMedia;
import tn.esprit.springfever.entities.Post;
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

    public CommentMedia save(MultipartFile file, Comment comment) throws Exception {
        String location = fileSystemRepository.save(file);
        return repo.save(new CommentMedia(file.getOriginalFilename(), location, comment, file.getBytes()));
    }
    @Cacheable("commentMedia")
    public FileSystemResource find(Long imageId) {
        CommentMedia image = repo.findById(Long.valueOf(imageId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return fileSystemRepository.findInFileSystem(image.getLocation());
    }

    @Override
    @CacheEvict("commentMedia")
    public void delete(Long id) {
        CommentMedia image = repo.findById(Long.valueOf(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        fileSystemRepository.deletefile(image.getLocation());
        repo.delete(image);
    }
}