package ru.comavp.dashboard.mappers;

import org.mapstruct.Mapper;
import ru.comavp.dashboard.dto.InvestTransactionDto;
import ru.comavp.dashboard.model.InvestTransaction;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvestTransactionMapper {

    InvestTransactionDto mapToDto(InvestTransaction entity);
    List<InvestTransactionDto> mapToDtoList(List<InvestTransaction> entityList);
    InvestTransaction mapToEntity(InvestTransactionDto dto);
    List<InvestTransaction> mapToEntityList(List<InvestTransactionDto> dtoList);
}
