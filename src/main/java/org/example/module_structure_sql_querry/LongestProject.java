package org.example.module_structure_sql_querry;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LongestProject {
    private Long id;
    private BigDecimal startMonth;
    private BigDecimal finishMonth;
}
