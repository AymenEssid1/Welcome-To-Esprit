package tn.esprit.springfever.Services.Interfaces;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.DTO.Job_RDV_DTO;
import tn.esprit.springfever.entities.Job_RDV;

@Mapper(componentModel = "spring")

@Service
public interface JobMapper {
    JobMapper INSTANCE = Mappers.getMapper(JobMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "salle_Rdv", source = "salleRdv")
    void updateClaimFromDto(Job_RDV_DTO job_rdv_dto, @MappingTarget Job_RDV job_rdv);

}



