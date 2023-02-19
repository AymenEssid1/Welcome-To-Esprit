package tn.esprit.springfever.repositories;

 import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Repository
public class FileSystemRepository {



    public String save(byte[] content, String imageName) throws Exception {
         Path newFile = Paths.get("C:\\Users\\User\\Desktop\\Images" + new Date().getTime() + "-" + imageName); // to change
         Files.createDirectories(newFile.getParent());
        Files.write(newFile, content);
        return newFile.toAbsolutePath()
                .toString();
    }



    public FileSystemResource findInFileSystem(String location) {
        try {
            return new FileSystemResource(Paths.get(location));
        } catch (Exception e) {
            // Handle access or file not found problems.
            throw new RuntimeException();
        }
    }
}