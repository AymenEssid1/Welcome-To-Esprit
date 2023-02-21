package tn.esprit.springfever.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import tn.esprit.springfever.entities.AdMedia;
import tn.esprit.springfever.enums.Channel;
import tn.esprit.springfever.enums.TypeOfAudience;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
public class AdDTO {

    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Channel channel;

    private Date startDate;
    private Date endDate;

    private int initialTargetNumberOfViews;
    private int finalNumberofViews;
    private float cost;
    private TypeOfAudience typeOfAudience;
    private List<AdMedia> media;
}
