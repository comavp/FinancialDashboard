package ru.comavp.dashboard.model.mappers;

import org.mapstruct.Mapper;
import ru.comavp.dashboard.model.dto.ReplenishmentTransactionDto;
import ru.comavp.dashboard.model.entity.ReplenishmentTransaction;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReplenishmentMapper {

    ReplenishmentTransactionDto toDto(ReplenishmentTransaction entity);
    List<ReplenishmentTransactionDto> toDtoList(List<ReplenishmentTransaction> entityList);
    ReplenishmentTransaction toEntity(ReplenishmentTransactionDto dto);
    List<ReplenishmentTransaction> toEntityList(List<ReplenishmentTransactionDto> dtoList);
}
