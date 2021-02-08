package com.project.orderprocessing.controller;

import com.project.orderprocessing.dto.OrderCreateDTO;
import com.project.orderprocessing.dto.OrderGetDTO;
import com.project.orderprocessing.repository.CustomerRepository;
import com.project.orderprocessing.service.OrderProcessingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Log4j2
public class OrderProcessingController {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OrderProcessingService orderProcessingService;

    @PostMapping("/create/order")
    public ResponseEntity orderCreate(@RequestBody OrderCreateDTO orderCreateDTO) {
        String returnValue = orderProcessingService.orderCreateService(orderCreateDTO);
        return returnValue.equals("Order Placed!")
                ? new ResponseEntity(returnValue, HttpStatus.CREATED)
                : new ResponseEntity(returnValue, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/cancel/order/{orderId}")
    public ResponseEntity orderCancel(@PathVariable String orderId) {
        String returnValue = orderProcessingService.orderCancelService(UUID.fromString(orderId));
        return returnValue.equals("Order Successfully Cancelled!")
                ? new ResponseEntity(returnValue, HttpStatus.OK)
                : new ResponseEntity(returnValue, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get/order/{orderId}")
    public ResponseEntity orderGet(@PathVariable String orderId) {
        OrderGetDTO orderGetDTO = orderProcessingService.orderGetService(UUID.fromString(orderId));
        return orderGetDTO.getFailureMessage() == null
                ? new ResponseEntity(orderGetDTO, HttpStatus.OK)
                : new ResponseEntity(orderGetDTO.getFailureMessage(), HttpStatus.NOT_FOUND);

    }

}
