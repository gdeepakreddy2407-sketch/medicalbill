package com.deepak.BillingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalBill {
    public String name;
    public int age;
    public String consultationtype;
    public List<Bill> billList;
    public BigDecimal total;

}
