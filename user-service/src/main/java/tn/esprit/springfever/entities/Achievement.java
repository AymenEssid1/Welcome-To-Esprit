package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;


@Entity
@ToString
@Data
public class Achievement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Icon icon;

    @ManyToMany(mappedBy = "achis")
    @JsonIgnore
    private Collection<User> users;

    public Achievement() {
    }

    public enum Icon {
        LIKE("👍"),
        STAR("🌟");

        private String value;

        Icon(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
