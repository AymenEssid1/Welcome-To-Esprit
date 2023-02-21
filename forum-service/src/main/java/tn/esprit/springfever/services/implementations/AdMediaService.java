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
import tn.esprit.springfever.entities.Ad;
import tn.esprit.springfever.entities.AdMedia;
import tn.esprit.springfever.entities.CommentMedia;
import tn.esprit.springfever.repositories.AdMediaRepository;
import tn.esprit.springfever.repositories.CommentMediaRepository;
import tn.esprit.springfever.repositories.FileSystemRepository;
import tn.esprit.springfever.services.interfaces.IAdMediaService;
@Service
@Slf4j
public class AdMediaService implements IAdMediaService {
    @Autowired
    FileSystemRepository fileSystemRepository;
    @Autowired
    AdMediaRepository repo;
    @Override
    public AdMedia save(MultipartFile file, Ad ad) throws Exception {
        String location = fileSystemRepository.save(file);
        return repo.save(new AdMedia(file.getOriginalFilename(), location, ad, file.getBytes()));
    }

    @Override
    @Cacheable("adMedia")
    public FileSystemResource find(Long imageId) {
        AdMedia image = repo.findById(Long.valueOf(imageId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return fileSystemRepository.findInFileSystem(image.getLocation());
    }

    @Override
    @CacheEvict("adMedia")
    public void delete(Long id) {
        AdMedia image = repo.findById(Long.valueOf(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        fileSystemRepository.deletefile(image.getLocation());
        repo.delete(image);
    }
}
