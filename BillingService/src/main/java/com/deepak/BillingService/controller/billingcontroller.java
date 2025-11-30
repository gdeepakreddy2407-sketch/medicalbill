package com.deepak.BillingService.controller;

import com.deepak.BillingService.dto.Request;
import com.deepak.BillingService.dto.TotalBill;
import com.deepak.BillingService.service.BillingService;
import com.deepak.BillingService.service.PdfGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class billingcontroller {
    @Autowired
    BillingService billingService;
    @Autowired
    PdfGenerationService pdfGenerationService;

    @PostMapping("/bill")  // Changed from @GetMapping to @PostMapping
    public TotalBill addProduct(@RequestBody Request productRequest){
        TotalBill message = billingService.addProduct(productRequest);
        return message;
    }

    @PostMapping("/bill/pdf")
    public ResponseEntity<byte[]> generateBillPdf(@RequestBody Request productRequest) {
        // Generate bill data
        TotalBill totalBill = billingService.addProduct(productRequest);

        // Generate PDF
        byte[] pdfBytes = pdfGenerationService.generateBillPdf(totalBill);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "medical_bill_" + totalBill.getName().replace(" ", "_") + ".pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/bill/pdf/view")
    public ResponseEntity<byte[]> generateBillPdfView(@RequestBody Request productRequest) {
        TotalBill totalBill = billingService.addProduct(productRequest);
        byte[] pdfBytes = pdfGenerationService.generateBillPdf(totalBill);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "medical_bill_" + totalBill.getName().replace(" ", "_") + ".pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}