package com.project.orderprocessing;

import com.project.orderprocessing.model.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class MockModelObjects {

    public Optional<Orders> getOrder() {
        Orders order = new Orders();
        order.setId(UUID.fromString("bb256f48-6b5a-11eb-9439-0242ac130002"));
        order.setCustomers(getCustomer().get());
        order.setOrderStatus(getOrderStatus().get());
        order.setShipping(getShipping().get());
        order.setAmountPreTax(new BigDecimal(599));
        order.setTaxAmount(new BigDecimal(20));
        order.setShippingAmount(new BigDecimal(10));
        order.setTotalAmount(new BigDecimal(629));
        order.setCreatedDate(new Date());
        order.setModifiedDate(new Date());
        return Optional.of(order);
    }

    public Optional<Customers> getCustomer() {
        Customers customer = new Customers();
        customer.setId(UUID.fromString("14254e40-6b58-11eb-9439-0242ac130002"));
        customer.setFirstName("Aliss");
        customer.setLastName("Bond");
        customer.setAddressLine1("1234 Test Street");
        customer.setAddressLine2("Apt 0001");
        customer.setCity("Dallas");
        customer.setState("Texas");
        customer.setEmailId("aliss@email.com");
        customer.setZip(70000);
        return Optional.of(customer);
    }

    public Optional<Items> getItem() {
        Items item = new Items();
        item.setId(UUID.fromString("24736a7e-6b59-11eb-9439-0242ac130002"));
        item.setName("Apple Air Pods Max");
        item.setSinNo(1010);
        item.setPrice(new BigDecimal(599));
        item.setDescription("Apple Air Pods Max");
        return Optional.of(item);
    }

    public Optional<OrderStatus> getOrderStatus() {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setId(UUID.fromString("11921c06-6b5a-11eb-9439-0242ac130002"));
        orderStatus.setName("ORDERED");
        return Optional.of(orderStatus);
    }

    public Optional<Shipping> getShipping() {
        Shipping shipping = new Shipping();
        shipping.setId(UUID.fromString("95e7a476-6b5a-11eb-9439-0242ac130002"));
        shipping.setShippingType("Credit Card");
        return Optional.of(shipping);
    }

}
