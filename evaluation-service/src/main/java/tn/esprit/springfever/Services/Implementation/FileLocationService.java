package tn.esprit.springfever.Services.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import tn.esprit.springfever.Services.Interfaces.IFileLocationService;
import tn.esprit.springfever.Services.Interfaces.IFileLocationService;
import tn.esprit.springfever.entities.ImageData;
import tn.esprit.springfever.entities.Video;
import tn.esprit.springfever.repositories.FileSystemRepository;
import tn.esprit.springfever.repositories.ImageDataRepository;
import tn.esprit.springfever.repositories.VideoRepository;



@Service
public class FileLocationService implements IFileLocationService {


    @Autowired
    FileSystemRepository fileSystemRepository;
    @Autowired
    ImageDataRepository imageDataRepository;
    @Autowired
    VideoRepository videoRepository;




     public ImageData save(byte[] bytes, String imageName) throws Exception {
        String location = fileSystemRepository.save(bytes, imageName);
        return imageDataRepository.save(new ImageData(imageName, location));
    }
    public FileSystemResource find(Long imageId) {
        ImageData image = imageDataRepository.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return fileSystemRepository.findInFileSystem(image.getLocation());
    }
    public Video saveVideo(byte[] bytes, String videoName)throws Exception{
        String location = fileSystemRepository.saveVideo(bytes, videoName);
        return videoRepository.save(new Video(videoName, location));
    }

    public FileSystemResource findVideo(Long idVideo){
        Video video = videoRepository.findById(idVideo).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return fileSystemRepository.findInFileSystem(video.getLocation());
    }


}
