package com.project.orderprocessing;

import com.project.orderprocessing.model.*;

import java.math.BigDecimal;
import java.util.*;

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

        List<OrderItems> orderItems = new ArrayList<>();
        orderItems.add(getOrderItem().get());
        order.setOrderItemsList(orderItems);

        List<Payments> paymentsList = new ArrayList<>();
        paymentsList.add(getPayments().get());
        order.setPaymentsList(paymentsList);

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
        shipping.setShippingType("Home Delivery");
        return Optional.of(shipping);
    }

    public Optional<Payments> getPayments() {
        Payments payment = new Payments();
        payment.setId(UUID.fromString("c2c3e468-6b6a-11eb-9439-0242ac130002"));
        payment.setConfirmationNumber("edc968b8-6b6a-11eb-9439-0242ac130002");
        payment.setCardNo("1234567891234567");
        payment.setPaymentsType(getPaymentType().get());
        payment.setAmount(new BigDecimal(629));
        payment.setAddressLine1("1234 Test Street");
        payment.setAddressLine2("Apt 0001");
        payment.setCity("Dallas");
        payment.setState("Texas");
        payment.setZip(70000);
        return Optional.of(payment);
    }

    public Optional<PaymentsType> getPaymentType() {
        PaymentsType paymentsType = new PaymentsType();
        paymentsType.setId(UUID.fromString("1e3b0da2-6b6c-11eb-9439-0242ac130002"));
        paymentsType.setPaymentType("Credit Card");
        return Optional.of(paymentsType);
    }

    public Optional<OrderItems> getOrderItem() {
        OrderItems orderItems = new OrderItems();
        orderItems.setId(UUID.fromString("e4f70978-6b6c-11eb-9439-0242ac130002"));
        orderItems.setItems(getItem().get());
        orderItems.setQuantity(1);
        return Optional.of(orderItems);
    }

}
