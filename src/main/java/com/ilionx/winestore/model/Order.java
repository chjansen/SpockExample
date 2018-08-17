package com.ilionx.winestore.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {

    private Customer customer;
    private Long numberOfProduct;
    private Double orderPrice;
    private Double vat;
    private List<OrderLine> orderLines;
    private String orderNumber;
    private Address shippingAddress;
}
