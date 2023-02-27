package tn.esprit.springfever.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.springfever.entities.Media;
import tn.esprit.springfever.repositories.FileSystemRepository;
import tn.esprit.springfever.repositories.MediaRepository;
import tn.esprit.springfever.services.interfaces.IMediaService;

@Service
public class MediaService implements IMediaService {
    @Autowired
    FileSystemRepository fileSystemRepository;
    @Autowired
    MediaRepository repo;
    @Override
    public Media save(MultipartFile file) throws Exception {
        String location = fileSystemRepository.save(file);
        return repo.save(new Media(file.getOriginalFilename(), location, file.getBytes(),file.getContentType()));
    }

    @Override
    public FileSystemResource find(Long imageId) {
        Media image = repo.findById(Long.valueOf(imageId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return fileSystemRepository.findInFileSystem(image.getLocation());
    }

    @Override
    public void delete(Long id) {
        Media  image = repo.findById(Long.valueOf(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        fileSystemRepository.deletefile(image.getLocation());
        repo.delete(image);
    }
}
