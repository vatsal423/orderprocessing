package com.project.orderprocessing.service;

import com.project.orderprocessing.MockModelObjects;
import com.project.orderprocessing.repository.OrderRepository;
import com.project.orderprocessing.repository.OrderStatusRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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


    MockModelObjects modelObjects;

    @Test
    public void orderCancelServiceTest() {
        modelObjects = new MockModelObjects();
        UUID uuid = UUID.fromString("bb256f48-6b5a-11eb-9439-0242ac130002");
        when(orderRepository.findById(uuid)).thenReturn(modelObjects.getOrder());
        when(orderStatusRepository.getStatusByName("ORDERED")).thenReturn(modelObjects.getOrderStatus().get());
        String result = orderProcessingService.orderCancelService(uuid);
        assertEquals("Order Successfully Cancelled!", result);
    }

}
