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
            if (customer.isPresent()) {
                log.error("Invalid Customer!");
                return "Invalid Customer!";
            } else
                order.setCustomers(customer.get());

            OrderStatus orderStatus = orderStatusRepository.getStatusByName("ORDERED");
            order.setOrderStatus(orderStatus);

            Optional<Shipping> shipping = shippingRepository.findById(UUID.fromString(orderCreateDTO.getShippingId()));
            if (shipping.isPresent()) {
                log.error("Invalid Shipping Method!");
                return "Invalid Shipping Method!";
            } else
                order.setShipping(shipping.get());

            BigDecimal preTaxAmount = new BigDecimal(0);
            List<ItemCreateDTO> itemCreateDTOList = orderCreateDTO.getItems();
            for (int i = 0; i < itemCreateDTOList.size(); i++) {
                Optional<Items> item = itemRepository.findById(UUID.fromString(itemCreateDTOList.get(i).getItemId()));
                if (item.isPresent()) {
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
        for (int i = 0; i < itemCreateDTOList.size(); i++) {
            OrderItems orderItem = new OrderItems();
            orderItem.setId(UUID.randomUUID());
            orderItem.setOrders(order);
            Optional<Items> item = itemRepository.findById(UUID.fromString(itemCreateDTOList.get(i).getItemId()));
            orderItem.setItems(item.get());
            orderItem.setQuantity(itemCreateDTOList.get(i).getQuantity());
            orderItemRepository.save(orderItem);
        }

        List<PaymentCreateDTO> paymentCreateDTOList = orderCreateDTO.getPayments();
        for (int i = 0; i < paymentCreateDTOList.size(); i++) {
            Payments payment = new Payments();
            payment.setId(UUID.randomUUID());
            Optional<PaymentsType> paymentsType = paymentsTypeRepository.findById(UUID.fromString(paymentCreateDTOList.get(i).getPaymentTypeId()));
            if (paymentsType.isPresent()) {
                log.error("Invalid Payment Method!");
                return "Invalid Payment Method!";
            } else
                payment.setPaymentsType(paymentsType.get());
            payment.setOrders(order);
            payment.setAddressLine1(paymentCreateDTOList.get(i).getAddressLine1());
            payment.setCardNo(paymentCreateDTOList.get(i).getCardNo());
            payment.setAddressLine2(paymentCreateDTOList.get(i).getAddressLine2());
            payment.setCity(paymentCreateDTOList.get(i).getCity());
            payment.setState(paymentCreateDTOList.get(i).getState());
            payment.setZip(paymentCreateDTOList.get(i).getZip());
            payment.setAmount(paymentCreateDTOList.get(i).getAmount());
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
            if (optionalOrder.isPresent()) {
                log.error("Invalid Order!");
                return "Invalid Order!";
            } else {
                Orders order = optionalOrder.get();
                OrderStatus orderStatus = orderStatusRepository.getStatusByName("ORDER CANCELLED");
                order.setOrderStatus(orderStatus);
                order.setModifiedDate(new Date());
                Orders insertedOrder = orderRepository.save(order);
                log.info("Order Cancelled!");
                return "Order Cancelled!";
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
            if(optionalOrder.isPresent()){
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
            for(int i=0;i<order.getOrderItemsList().size();i++){
                ItemGetDTO itemGetDTO = new ItemGetDTO();
                itemGetDTO.setItemName(order.getOrderItemsList().get(i).getItems().getName());
                itemGetDTO.setQuantity(order.getOrderItemsList().get(i).getQuantity());
                itemGetDTOList.add(itemGetDTO);
            }
            orderGetDTO.setItems(itemGetDTOList);

            List<PaymentGetDTO> paymentGetDTOList = new ArrayList<>();
            for (int i=0;i<order.getPaymentsList().size();i++){
                PaymentGetDTO paymentGetDTO = new PaymentGetDTO();
                paymentGetDTO.setPaymentType(order.getPaymentsList().get(i).getPaymentsType().getPaymentType());
                paymentGetDTO.setAmount(order.getPaymentsList().get(i).getAmount());
                paymentGetDTO.setCardNo(order.getPaymentsList().get(i).getCardNo());
                paymentGetDTO.setAddressLine1(order.getPaymentsList().get(i).getAddressLine1());
                paymentGetDTO.setAddressLine2(order.getPaymentsList().get(i).getAddressLine2());
                paymentGetDTO.setCity(order.getPaymentsList().get(i).getCity());
                paymentGetDTO.setState(order.getPaymentsList().get(i).getState());
                paymentGetDTO.setZip(order.getPaymentsList().get(i).getZip());
                paymentGetDTOList.add(paymentGetDTO);
            }
            orderGetDTO.setPayments(paymentGetDTOList);

            log.info("Order Details Fetched!");
            orderGetDTO.setSuccessMesage("Order Details Fetched!");
            return orderGetDTO;

        }catch(Exception e){
            OrderGetDTO orderGetDTO = new OrderGetDTO();
            log.error("Error while fecthing the Order Details",e);
            orderGetDTO.setFailureMessage("Error while fecthing the Order Details");
            return orderGetDTO;
        }
    }
}