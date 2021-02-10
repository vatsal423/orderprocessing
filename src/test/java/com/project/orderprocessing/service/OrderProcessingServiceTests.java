package com.project.orderprocessing.service;

import com.project.orderprocessing.MockModelObjects;
import com.project.orderprocessing.dto.OrderGetDTO;
import com.project.orderprocessing.repository.OrderRepository;
import com.project.orderprocessing.repository.OrderStatusRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
public class OrderProcessingServiceTests {

    @Autowired
    OrderProcessingService orderProcessingService;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    OrderStatusRepository orderStatusRepository;

    private MockModelObjects mockModelObjects;

    @Test
    public void testOrderCancelServiceSuccessfulCancelled() {
        mockModelObjects = new MockModelObjects();
        UUID uuid = UUID.fromString("bb256f48-6b5a-11eb-9439-0242ac130002");
        when(orderRepository.findById(uuid)).thenReturn(mockModelObjects.getOrder());
        when(orderStatusRepository.getStatusByName("ORDERED")).thenReturn(mockModelObjects.getOrderStatus().get());
        String result = orderProcessingService.orderCancelService(uuid);
        assertEquals("Order Successfully Cancelled!", result);
    }

    @Test
    public void testOrderCancelServiceInvalidOrder() {
        mockModelObjects = new MockModelObjects();
        UUID uuid = UUID.fromString("bb256f48-6b5a-11eb-9439-0242ac130002");
        when(orderRepository.findById(uuid)).thenReturn(mockModelObjects.getOrder());
        String result = orderProcessingService.orderCancelService(UUID.fromString("92bf9b82-6b64-11eb-9439-0242ac130002"));
        assertEquals("Invalid Order!", result);
    }

    @Test
    public void testOrderGetServiceInvalidOrder() {
        mockModelObjects = new MockModelObjects();
        UUID uuid = UUID.fromString("bb256f48-6b5a-11eb-9439-0242ac130002");
        when(orderRepository.findById(uuid)).thenReturn(mockModelObjects.getOrder());
        OrderGetDTO orderGetDTO = orderProcessingService.orderGetService(UUID.fromString("92bf9b82-6b64-11eb-9439-0242ac130002"));
        assertEquals("Invalid Order Id!", orderGetDTO.getFailureMessage());
    }

    @Test
    public void testOrderGetServiceOrderDetailsFetched() {
        mockModelObjects = new MockModelObjects();
        UUID uuid = UUID.fromString("bb256f48-6b5a-11eb-9439-0242ac130002");
        when(orderRepository.findById(uuid)).thenReturn(mockModelObjects.getOrder());
        OrderGetDTO orderGetDTO = orderProcessingService.orderGetService(uuid);
        assertEquals("Order Details Fetched!", orderGetDTO.getSuccessMesage());
        assertEquals("Aliss Bond", orderGetDTO.getCustomerName());
        assertEquals("ORDERED", orderGetDTO.getOrderStatus());
        assertEquals("Home Delivery", orderGetDTO.getShippingType());
        assertEquals(new BigDecimal(629), orderGetDTO.getTotalAmount());
        assertEquals("1234567891234567", orderGetDTO.getPayments().get(0).getCardNo());
        assertEquals("Credit Card", orderGetDTO.getPayments().get(0).getPaymentType());
        assertEquals("Apple Air Pods Max", orderGetDTO.getItems().get(0).getItemName());
        assertEquals(1, orderGetDTO.getItems().get(0).getQuantity());
    }

}
