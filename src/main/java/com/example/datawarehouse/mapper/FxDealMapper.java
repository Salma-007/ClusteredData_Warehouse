package com.example.datawarehouse.mapper;

import com.example.datawarehouse.dto.request.FxDealRequest;
import com.example.datawarehouse.dto.response.FxDealResponse;
import com.example.datawarehouse.model.FxDeal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FxDealMapper {

    FxDeal toEntity(FxDealRequest request);

    FxDealResponse toResponse(FxDeal entity);
}
