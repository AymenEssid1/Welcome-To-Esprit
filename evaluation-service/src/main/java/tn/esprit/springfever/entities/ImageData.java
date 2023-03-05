package tn.esprit.springfever.entities;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;


@Entity
@Data
public class ImageData {

    @Id
    @GeneratedValue
    Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Location cannot be blank")
    private String location;

    @Lob
    byte[] content;

    public ImageData(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public ImageData(String name, byte[] content) {
        this.name = name;
        this.content = content;
    }

    public ImageData() {
    }

}
