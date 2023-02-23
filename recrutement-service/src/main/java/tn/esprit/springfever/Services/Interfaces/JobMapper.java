package tn.esprit.springfever.Services.Interfaces;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.DTO.Job_RDV_DTO;
import tn.esprit.springfever.entities.Job_RDV;

@Mapper(componentModel = "spring")
@Service
public interface JobMapper {
    JobMapper INSTANCE = Mappers.getMapper(JobMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "salle_Rdv", source = "salleRdv")

    void updateClaimFromDto(@MappingTarget Job_RDV job_rdv, Job_RDV_DTO job_rdv_dto);
}

