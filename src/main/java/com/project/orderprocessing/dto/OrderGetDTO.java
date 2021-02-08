package com.project.orderprocessing.dto;

import com.project.orderprocessing.model.Customers;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderGetDTO {
    private String customerName;
    private List<ItemGetDTO> items;
    private String shippingType;
    private String orderStatus;
    private BigDecimal amountPreTax;
    private BigDecimal taxAmount;
    private BigDecimal shippingCharges;
    private BigDecimal totalAmount;
    private List<PaymentGetDTO> payments;
    private String successMesage;
    private String failureMessage;
}
