package com.deepak.BillingService.service;

import com.deepak.BillingService.dto.Bill;
import com.deepak.BillingService.dto.TotalBill;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGenerationService {

    public byte[] generateBillPdf(TotalBill totalBill) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Header Section
            addHeader(document);

            // Patient Information
            addPatientInfo(document, totalBill);

            // Medicine Details Table
            addMedicineTable(document, totalBill);

            // Total Section
            addTotalSection(document, totalBill);

            // Footer
            addFooter(document);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    private void addHeader(Document document) {
        // Clinic Name
        Paragraph clinicName = new Paragraph("HEALTHCARE CLINIC")
                .setFontSize(24)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(new DeviceRgb(41, 128, 185));

        Paragraph address = new Paragraph("123 Medical Street, City - 500001\nPhone: +91-1234567890 | Email: info@healthcare.com")
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);

        // Bill Title
        Paragraph billTitle = new Paragraph("MEDICAL BILL")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(10)
                .setMarginBottom(5);

        // Date and Bill Number
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        String billNumber = "BILL-" + System.currentTimeMillis();

        Table headerTable = new Table(2);
        headerTable.setWidth(UnitValue.createPercentValue(100));

        headerTable.addCell(new Cell().add(new Paragraph("Bill No: " + billNumber).setFontSize(10))
                .setBorder(Border.NO_BORDER));
        headerTable.addCell(new Cell().add(new Paragraph("Date: " + currentDate).setFontSize(10))
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT));

        document.add(clinicName);
        document.add(address);
        document.add(billTitle);
        document.add(headerTable);
        document.add(new Paragraph("\n"));
    }

    private void addPatientInfo(Document document, TotalBill totalBill) {
        // Patient Info Box
        Table patientTable = new Table(2);
        patientTable.setWidth(UnitValue.createPercentValue(100));
        patientTable.setMarginBottom(20);

        DeviceRgb lightBlue = new DeviceRgb(230, 240, 250);

        patientTable.addCell(new Cell().add(new Paragraph("Patient Name:").setBold())
                .setBackgroundColor(lightBlue)
                .setPadding(5));
        patientTable.addCell(new Cell().add(new Paragraph(totalBill.getName()))
                .setPadding(5));

        patientTable.addCell(new Cell().add(new Paragraph("Age:").setBold())
                .setBackgroundColor(lightBlue)
                .setPadding(5));
        patientTable.addCell(new Cell().add(new Paragraph(String.valueOf(totalBill.getAge())))
                .setPadding(5));

        patientTable.addCell(new Cell().add(new Paragraph("Consultation Type:").setBold())
                .setBackgroundColor(lightBlue)
                .setPadding(5));
        patientTable.addCell(new Cell().add(new Paragraph(totalBill.getConsultationtype()))
                .setPadding(5));

        document.add(patientTable);
    }

    private void addMedicineTable(Document document, TotalBill totalBill) {
        // Medicine Table Header
        float[] columnWidths = {1, 3, 3, 2};
        Table table = new Table(columnWidths);
        table.setWidth(UnitValue.createPercentValue(100));

        DeviceRgb headerColor = new DeviceRgb(41, 128, 185);

        // Headers
        table.addHeaderCell(new Cell().add(new Paragraph("S.No").setBold().setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(headerColor)
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(8));
        table.addHeaderCell(new Cell().add(new Paragraph("Medicine Name").setBold().setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(headerColor)
                .setPadding(8));
        table.addHeaderCell(new Cell().add(new Paragraph("Use Case").setBold().setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(headerColor)
                .setPadding(8));
        table.addHeaderCell(new Cell().add(new Paragraph("Price (₹)").setBold().setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(headerColor)
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(8));

        // Add medicine rows
        int serialNo = 1;
        DeviceRgb altRowColor = new DeviceRgb(245, 245, 245);
        DeviceRgb whiteColor = new DeviceRgb(255, 255, 255);

        for (Bill bill : totalBill.getBillList()) {
            DeviceRgb rowColor = (serialNo % 2 == 0) ? altRowColor : whiteColor;

            table.addCell(new Cell().add(new Paragraph(String.valueOf(serialNo)))
                    .setBackgroundColor(rowColor)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(8));
            table.addCell(new Cell().add(new Paragraph(bill.getMedicine()))
                    .setBackgroundColor(rowColor)
                    .setPadding(8));
            table.addCell(new Cell().add(new Paragraph(bill.getUsecase()))
                    .setBackgroundColor(rowColor)
                    .setPadding(8));
            table.addCell(new Cell().add(new Paragraph("₹ " + bill.getPrice()))
                    .setBackgroundColor(rowColor)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setPadding(8));

            serialNo++;
        }

        document.add(table);
    }

    private void addTotalSection(Document document, TotalBill totalBill) {
        document.add(new Paragraph("\n"));

        // Total Amount Box
        Table totalTable = new Table(2);
        totalTable.setWidth(UnitValue.createPercentValue(100));

        DeviceRgb totalColor = new DeviceRgb(46, 204, 113);

        totalTable.addCell(new Cell().add(new Paragraph("Total Amount:").setBold().setFontSize(14))
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(10));
        totalTable.addCell(new Cell().add(new Paragraph("₹ " + totalBill.getTotal()).setBold().setFontSize(14))
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(totalColor)
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(10));

        document.add(totalTable);
    }

    private void addFooter(Document document) {
        document.add(new Paragraph("\n\n"));

        Paragraph terms = new Paragraph("Terms & Conditions:")
                .setBold()
                .setFontSize(10)
                .setMarginTop(20);

        Paragraph termsContent = new Paragraph(
                "1. Please take medicines as prescribed by the doctor.\n" +
                        "2. Keep this bill for future reference.\n" +
                        "3. In case of any discrepancy, please contact within 24 hours.")
                .setFontSize(9)
                .setMarginBottom(20);

        Paragraph signature = new Paragraph("Authorized Signature: _________________")
                .setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT);

        Paragraph thankyou = new Paragraph("Thank you for choosing our healthcare services!")
                .setFontSize(10)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(new DeviceRgb(41, 128, 185))
                .setMarginTop(10);

        document.add(terms);
        document.add(termsContent);
        document.add(signature);
        document.add(thankyou);
    }
}