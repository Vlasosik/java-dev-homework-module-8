package org.example.module_structure_sql_querry;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProjectPrices {
    private Long workerId;
    private BigDecimal price;
}
