package tn.esprit.springfever.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.PostMedia;
import tn.esprit.springfever.repositories.FileSystemRepository;
import tn.esprit.springfever.repositories.PostMediaRepository;
import tn.esprit.springfever.services.interfaces.IPostMediaService;

@Service
@Slf4j
public class PostMediaService implements IPostMediaService {
    @Autowired
    FileSystemRepository fileSystemRepository;
    @Autowired
    PostMediaRepository repo;

    public PostMedia save(MultipartFile file, Post post) throws Exception {
        String location = fileSystemRepository.save(file);
        return repo.save(new PostMedia(file.getOriginalFilename(), location, post));
    }
    public FileSystemResource find(Long imageId) {
        PostMedia image = repo.findById(Long.valueOf(imageId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return fileSystemRepository.findInFileSystem(image.getLocation());
    }

    @Override
    public String delete(Long id) {
        PostMedia image = repo.findById(Long.valueOf(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        fileSystemRepository.deletefile(image.getLocation());
        return "file deleted";
    }
}