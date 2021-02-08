package com.project.orderprocessing.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCreateDTO {

    private String customerId;
    private String shippingId;
    private List<ItemCreateDTO> items;
    private List<PaymentCreateDTO> payments;
}