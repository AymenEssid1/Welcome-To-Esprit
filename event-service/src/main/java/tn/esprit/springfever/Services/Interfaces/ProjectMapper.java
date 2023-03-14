package tn.esprit.springfever.Services.Interfaces;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.DTO.ProjectDTO;
import tn.esprit.springfever.entities.Project;
@Mapper(componentModel = "spring")

@Service
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProjectFromDto(ProjectDTO projectDto, @MappingTarget Project project);

}
