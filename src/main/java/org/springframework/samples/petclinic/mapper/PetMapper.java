package org.springframework.samples.petclinic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.samples.petclinic.dto.PetDto;
import org.springframework.samples.petclinic.dto.PetTypeDto;
import org.springframework.samples.petclinic.dto.VisitDto;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;

import java.util.Collection;

/**
 * Map Pet & PetDto using mapstruct
 */
@Mapper
public interface PetMapper {

    // Issue 3: map the nested type.id to the flat typeId field in PetDto
    @Mapping(target = "typeId", source = "type.id")
    PetDto toPetDto(Pet pet);

    Collection<PetDto> toPetsDto(Collection<Pet> pets);

    Collection<Pet> toPets(Collection<PetDto> pets);

    // Issue 1: Pet entity has an 'owner' field that has no counterpart in PetDto — ignore it
    @Mapping(target = "owner", ignore = true)
    Pet toPet(PetDto petDto);

    PetTypeDto toPetTypeDto(PetType petType);

    PetType toPetType(PetTypeDto petTypeDto);

    Collection<PetTypeDto> toPetTypeDtos(Collection<PetType> petTypes);

    // Issue 2: Visit entity has a 'pet' back-reference that has no counterpart in VisitDto — ignore it.
    // This explicit method is used when MapStruct maps the visits collection inside toPet/toPets.
    @Mapping(target = "pet", ignore = true)
    Visit toVisit(VisitDto visitDto);
}
