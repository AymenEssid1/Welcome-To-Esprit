package tn.esprit.springfever.Services.Interfaces;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.DTO.EventDTO;
import tn.esprit.springfever.entities.Event;


@Mapper(componentModel = "spring")

@Service
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEventFromDto(EventDTO eventDTO, @MappingTarget Event event);

}
