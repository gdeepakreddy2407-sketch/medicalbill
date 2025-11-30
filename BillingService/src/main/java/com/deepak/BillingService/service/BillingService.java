package com.deepak.BillingService.service;

import com.deepak.BillingService.Entity;
import com.deepak.BillingService.dto.Bill;
import com.deepak.BillingService.dto.Request;
import com.deepak.BillingService.dto.TotalBill;
import com.deepak.BillingService.repository.BillingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillingService {
    @Autowired
    private BillingRepository billingRepository;
    public TotalBill addProduct(Request productRequest) {
        List<String> medicineNamesList = productRequest.getRequestList().stream()
                .map(requestItem -> requestItem.getMedicine())
                .collect(Collectors.toList());
        List<Entity> medicines = billingRepository.findByNameIn(medicineNamesList);
        TotalBill total = new TotalBill();
        total.setName(productRequest.getName());
        total.setAge(productRequest.getAge());
        total.setConsultationtype(productRequest.getConsultationtype());
        if (total.getBillList() == null) {
            total.setBillList(new ArrayList<>());
        }
        for (Entity medicineEntity : medicines) {
            Bill item = new Bill(); // Create a NEW item object in the loop
            item.setMedicine(medicineEntity.getName());
            item.setUsecase(medicineEntity.getCategory());
            item.setPrice(medicineEntity.getPrice());

            // Add the newly created item to the list managed by the total object
            total.getBillList().add(item);
        }

        total.setTotal(medicines.stream().map(y->y.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add));
        return total;
    }
}
