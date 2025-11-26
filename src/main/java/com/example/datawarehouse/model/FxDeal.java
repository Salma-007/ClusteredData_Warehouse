package com.example.datawarehouse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "fx_deal", uniqueConstraints = {
        @UniqueConstraint(columnNames = "deal_unique_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FxDeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deal_unique_id", nullable = false, length = 50)
    private String dealUniqueId;

    @Column(name = "from_currency_iso_code", nullable = false, length = 3)
    private String fromCurrencyIsoCode;

    @Column(name = "to_currency_iso_code", nullable = false, length = 3)
    private String toCurrencyIsoCode;

    @Column(name = "deal_timestamp", nullable = false)
    private Instant dealTimestamp;

    @Column(name = "deal_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal dealAmount;
}
