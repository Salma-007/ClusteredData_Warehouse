package com.example.datawarehouse.repository;

import com.example.datawarehouse.model.FxDeal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FxDealRepository extends JpaRepository<FxDeal, Long> {
    boolean existsByDealUniqueId(String dealUniqueId);
}
