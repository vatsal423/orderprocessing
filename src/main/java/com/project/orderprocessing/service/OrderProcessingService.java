package com.project.orderprocessing.service;

import com.project.orderprocessing.dto.*;
import com.project.orderprocessing.model.*;
import com.project.orderprocessing.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderProcessingService {

    Logger log = LogManager.getLogger(OrderProcessingService.class);

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OrderStatusRepository orderStatusRepository;

    @Autowired
    ShippingRepository shippingRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PaymentsTypeRepository paymentsTypeRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    public String orderCreateService(OrderCreateDTO orderCreateDTO) {
        try {
            Orders order = new Orders();

            UUID orderId = UUID.randomUUID();
            order.setId(orderId);

            Optional<Customers> customer = customerRepository.findById(UUID.fromString(orderCreateDTO.getCustomerId()));
            if (!customer.isPresent()) {
                log.error("Invalid Customer!");
                return "Invalid Customer!";
            } else
                order.setCustomers(customer.get());

            OrderStatus orderStatus = orderStatusRepository.getStatusByName("ORDERED");
            order.setOrderStatus(orderStatus);

            Optional<Shipping> shipping = shippingRepository.findById(UUID.fromString(orderCreateDTO.getShippingId()));
            if (!shipping.isPresent()) {
                log.error("Invalid Shipping Method!");
                return "Invalid Shipping Method!";
            } else
                order.setShipping(shipping.get());

            BigDecimal preTaxAmount = new BigDecimal(0);
            List<ItemCreateDTO> itemCreateDTOList = orderCreateDTO.getItems();
            for (int i = 0; i < itemCreateDTOList.size(); i++) {
                Optional<Items> item = itemRepository.findById(UUID.fromString(itemCreateDTOList.get(i).getItemId()));
                if (!item.isPresent()) {
                    log.error("Invalid Item" + i + 1 + "!");
                    return "Invalid Item" + i + 1 + "!";
                } else {
                    BigDecimal quantity = new BigDecimal(itemCreateDTOList.get(i).getQuantity());
                    preTaxAmount = preTaxAmount.add(item.get().getPrice().multiply(quantity));
                }
            }
            order.setAmountPreTax(preTaxAmount);

            BigDecimal taxAmount = preTaxAmount.multiply(new BigDecimal(0.08));
            order.setTaxAmount(taxAmount);

            BigDecimal shippingAmount = new BigDecimal(10);
            order.setShippingAmount(shippingAmount);

            BigDecimal totalAmount = preTaxAmount.add(taxAmount).add(shippingAmount);
            order.setTotalAmount(totalAmount);

            Date date = new Date();
            order.setCreatedDate(date);
            order.setModifiedDate(date);

            Orders insertedOrder = orderRepository.save(order);

            return paymentItemCreateService(orderCreateDTO, insertedOrder);

        } catch (Exception e) {
            log.error("Error Placing Order", e);
            return "Error Placing Order";
        }
    }

    private String paymentItemCreateService(OrderCreateDTO orderCreateDTO, Orders order) {

        List<ItemCreateDTO> itemCreateDTOList = orderCreateDTO.getItems();
        for (ItemCreateDTO itemCreateDTO : itemCreateDTOList) {
            OrderItems orderItem = new OrderItems();
            orderItem.setId(UUID.randomUUID());
            orderItem.setOrders(order);
            Optional<Items> item = itemRepository.findById(UUID.fromString(itemCreateDTO.getItemId()));
            orderItem.setItems(item.get());
            orderItem.setQuantity(itemCreateDTO.getQuantity());
            orderItemRepository.save(orderItem);
        }

        List<PaymentCreateDTO> paymentCreateDTOList = orderCreateDTO.getPayments();
        for (PaymentCreateDTO paymentCreateDTO : paymentCreateDTOList) {
            Payments payment = new Payments();
            payment.setId(UUID.randomUUID());
            Optional<PaymentsType> paymentsType = paymentsTypeRepository.findById(UUID.fromString(paymentCreateDTO.getPaymentTypeId()));
            if (!paymentsType.isPresent()) {
                log.error("Invalid Payment Method!");
                return "Invalid Payment Method!";
            } else
                payment.setPaymentsType(paymentsType.get());
            payment.setOrders(order);
            payment.setAddressLine1(paymentCreateDTO.getAddressLine1());
            payment.setCardNo(paymentCreateDTO.getCardNo());
            payment.setAddressLine2(paymentCreateDTO.getAddressLine2());
            payment.setCity(paymentCreateDTO.getCity());
            payment.setState(paymentCreateDTO.getState());
            payment.setZip(paymentCreateDTO.getZip());
            payment.setAmount(paymentCreateDTO.getAmount());
            payment.setDate(new Date());
            payment.setConfirmationNumber(UUID.randomUUID().toString());
            paymentRepository.save(payment);
        }
        log.info("Order Placed");
        return "Order Placed!";
    }

    public String orderCancelService(UUID orderId) {
        try {
            Optional<Orders> optionalOrder = orderRepository.findById(orderId);
            if (!optionalOrder.isPresent()) {
                log.error("Invalid Order!");
                return "Invalid Order!";
            } else {
                Orders order = optionalOrder.get();
                OrderStatus orderStatus = orderStatusRepository.getStatusByName("ORDER CANCELLED");
                order.setOrderStatus(orderStatus);
                order.setModifiedDate(new Date());
                Orders insertedOrder = orderRepository.save(order);
                log.info("Order Successfully Cancelled!");
                return "Order Successfully Cancelled!";
            }
        } catch (Exception e) {
            log.error("Error While Cancelling Order",e);
            return "Error While Cancelling Order";
        }
    }

    public OrderGetDTO orderGetService(UUID orderId) {
        try{
            OrderGetDTO orderGetDTO = new OrderGetDTO();
            Optional<Orders> optionalOrder = orderRepository.findById(orderId);
            if (!optionalOrder.isPresent()) {
                log.error("Invalid Order Id!");
                orderGetDTO.setFailureMessage("Invalid Order Id!");
                return orderGetDTO;
            }
            Orders order = optionalOrder.get();
            orderGetDTO.setCustomerName(order.getCustomers().getFirstName() + " " + order.getCustomers().getLastName());
            orderGetDTO.setAmountPreTax(order.getAmountPreTax());
            orderGetDTO.setTaxAmount(order.getTaxAmount());
            orderGetDTO.setShippingCharges(order.getShippingAmount());
            orderGetDTO.setTotalAmount(order.getTotalAmount());
            orderGetDTO.setShippingType(order.getShipping().getShippingType());
            orderGetDTO.setOrderStatus(order.getOrderStatus().getName());

            List<ItemGetDTO> itemGetDTOList = new ArrayList<>();
            for (OrderItems orderItems : order.getOrderItemsList()) {
                ItemGetDTO itemGetDTO = new ItemGetDTO();
                itemGetDTO.setItemName(orderItems.getItems().getName());
                itemGetDTO.setQuantity(orderItems.getQuantity());
                itemGetDTOList.add(itemGetDTO);
            }
            orderGetDTO.setItems(itemGetDTOList);

            List<PaymentGetDTO> paymentGetDTOList = new ArrayList<>();
            for (Payments payments : order.getPaymentsList()) {
                PaymentGetDTO paymentGetDTO = new PaymentGetDTO();
                paymentGetDTO.setPaymentType(payments.getPaymentsType().getPaymentType());
                paymentGetDTO.setAmount(payments.getAmount());
                paymentGetDTO.setCardNo(payments.getCardNo());
                paymentGetDTO.setAddressLine1(payments.getAddressLine1());
                paymentGetDTO.setAddressLine2(payments.getAddressLine2());
                paymentGetDTO.setCity(payments.getCity());
                paymentGetDTO.setState(payments.getState());
                paymentGetDTO.setZip(payments.getZip());
                paymentGetDTOList.add(paymentGetDTO);
            }
            orderGetDTO.setPayments(paymentGetDTOList);

            log.info("Order Details Fetched!");
            orderGetDTO.setSuccessMesage("Order Details Fetched!");
            return orderGetDTO;

        }catch(Exception e) {
            OrderGetDTO orderGetDTO = new OrderGetDTO();
            log.error("Error while fetching the Order Details", e);
            orderGetDTO.setFailureMessage("Error while fetching the Order Details");
            return orderGetDTO;
        }
    }
}