package tn.esprit.springfever.services.interfaces;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import tn.esprit.springfever.dto.PostDTO;
import tn.esprit.springfever.entities.Post;

public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void addPostFromDTO(PostDTO postDto, @MappingTarget Post post);
}
