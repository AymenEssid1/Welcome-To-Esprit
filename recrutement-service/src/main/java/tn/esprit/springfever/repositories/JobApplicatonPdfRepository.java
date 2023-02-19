package tn.esprit.springfever.repositories;

import org.springframework.stereotype.Repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Repository
public class JobApplicatonPdfRepository {
    public String save(byte[] contentCv, byte[] contentLettre,String imageName) throws Exception {
        Path newFile = Paths.get("C:\\Users\\User\\Desktop\\ProjectHelp" + new Date().getTime() + "-" + imageName); // to change
        Files.createDirectories(newFile.getParent());
        Files.write(newFile, contentCv);
        Files.write(newFile,contentLettre);
        return newFile.toAbsolutePath()
                .toString();
    }
}
