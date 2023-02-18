package tn.esprit.springfever.Services.Interfaces;


 import org.mapstruct.BeanMapping;
 import org.mapstruct.Mapper;
 import org.mapstruct.MappingTarget;
 import org.mapstruct.NullValuePropertyMappingStrategy;
 import org.mapstruct.factory.Mappers;
 import org.springframework.stereotype.Service;
 import tn.esprit.springfever.DTO.ClaimDTO;
 import  tn.esprit.springfever.entities.Claim;
@Mapper(componentModel = "spring")

@Service
public interface ClaimMapper {
     ClaimMapper INSTANCE = Mappers.getMapper(ClaimMapper.class);

     @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
     void updateClaimFromDto(ClaimDTO claimDto, @MappingTarget Claim claim);
}