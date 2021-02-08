package com.project.orderprocessing.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCreateDTO {
    private String itemId;
    private  int quantity;
}
