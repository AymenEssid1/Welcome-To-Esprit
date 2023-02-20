package tn.esprit.springfever.Services.Interface;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.User;
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void addUserFromDTO(UserDTO userDTO, @MappingTarget User user);
}


