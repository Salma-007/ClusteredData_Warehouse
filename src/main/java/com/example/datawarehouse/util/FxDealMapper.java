package com.example.datawarehouse.util;

import com.example.datawarehouse.dto.FxDealRequestDTO;
import com.example.datawarehouse.model.FxDeal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FxDealMapper {

    FxDeal toEntity(FxDealRequestDTO requestDto);
}
