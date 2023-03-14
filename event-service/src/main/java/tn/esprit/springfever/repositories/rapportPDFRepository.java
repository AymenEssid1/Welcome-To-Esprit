package tn.esprit.springfever.repositories;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class rapportPDFRepository {

    public List<String> save(byte[] contentrapport, String location_rapport) throws Exception {
        Path rapportFile = Paths.get("C:\\Users\\Nour\\Desktop\\" + new Date().getTime() + "-" + location_rapport);

        Files.write(rapportFile, contentrapport);

        return Arrays.asList(rapportFile.toAbsolutePath().toString());
    }


    public static FileSystemResource findInFileSystem(String location_rapport) {
        try {


            return  new FileSystemResource(Paths.get(location_rapport));
        } catch (Exception e) {
            // Handle access or file not found problems.
            throw new RuntimeException();
        }
    }


}
