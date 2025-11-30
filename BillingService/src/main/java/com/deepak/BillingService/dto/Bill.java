package com.deepak.BillingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill {
    public String medicine;
    public String usecase;
    public BigDecimal price;
}
