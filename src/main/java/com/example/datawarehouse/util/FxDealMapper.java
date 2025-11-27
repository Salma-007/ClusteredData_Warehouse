package com.example.datawarehouse.util;

import com.example.datawarehouse.dto.request.FxDealRequest;
import com.example.datawarehouse.dto.response.FxDealResponse;
import com.example.datawarehouse.model.FxDeal;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FxDealMapper {

    FxDeal toEntity(FxDealRequest request);

    FxDealResponse toResponse(FxDeal entity);
}
