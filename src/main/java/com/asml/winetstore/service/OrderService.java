package com.asml.winetstore.service;

import com.asml.winetstore.model.Address;
import com.asml.winetstore.model.Order;
import com.asml.winetstore.model.OrderLine;
import com.asml.winetstore.model.Wine;
import com.asml.winetstore.repository.CustomerRepository;
import com.asml.winetstore.repository.WineRepository;

import java.util.Random;

public class OrderService {

    private final CustomerRepository customerRepository;
    private final WineRepository wineRepository;
    private Random rand = new Random();

    public OrderService(CustomerRepository customerRepository, WineRepository wineRepository) {
        this.customerRepository = customerRepository;
        this.wineRepository = wineRepository;
    }


    public Order createOrder(String customerId) {
        Order order = new Order();
        order.setCustomer(customerRepository.findCustomerById(customerId));
        order.setOrderNumber(customerId + rand.nextInt(99999));
        return order;
    }


    public Order addOrderLine(Order order, String wineName, Long numberOfBottles) {
        OrderLine orderLine = new OrderLine();
        Wine wine = wineRepository.findWineByName(wineName);
        orderLine.setWine(wine);
        orderLine.setDescription(wine.getName()
                + " ;" + wine.getVineyard()
                + " ;" + wine.getProductionYear()
                + " ;E " + wine.getPrice());
        orderLine.setNumber(numberOfBottles);
        orderLine.setTotal(wine.getPrice() * numberOfBottles);
        orderLine.setNumber(numberOfBottles);
        order.getOrderLines().add(orderLine);
        return order;
    }

    public Order completeOrder(Order order, Address address) {
        if (address == null) {
            address = order.getCustomer().getShippingAddress();
        }
        order.setShippingAddress(address);
        order.setOrderPrice(calculateOrderTotal(order));
        order.setNumberOfProduct(calculateNumberOfProducts(order));
        order.setVat(order.getOrderPrice()*0.21);
        return order;
    }

    private Double calculateOrderTotal(Order order) {
        Double orderTotal = 0.0D;
        for(OrderLine line: order.getOrderLines()) {
            orderTotal += line.getTotal();
        }
        return orderTotal;
    }

    private Long calculateNumberOfProducts(Order order) {
        Long numberOfProducts = 0L;
        for(OrderLine line: order.getOrderLines()) {
            numberOfProducts += line.getNumber();
        }
        return numberOfProducts;
    }

}
