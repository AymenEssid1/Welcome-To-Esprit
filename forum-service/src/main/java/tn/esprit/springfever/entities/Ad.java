package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import tn.esprit.springfever.enums.Channel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Advertisement")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@EqualsAndHashCode
public class Ad implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 200)
    private String name;

    @Enumerated(EnumType.STRING)
    private Channel content;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private int initialTargetNumberOfViews;
    private int finalNumberofViews;
    private float cost;
    @Enumerated(EnumType.STRING)
    private Channel typeOfAudience;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ad")
    @JsonIgnore
    private List<AdMedia> media;

}