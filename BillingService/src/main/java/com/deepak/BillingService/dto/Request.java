package com.deepak.BillingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    public String name;
    public int age;
    public String consultationtype;
    public List<RequestList> requestList;

}
