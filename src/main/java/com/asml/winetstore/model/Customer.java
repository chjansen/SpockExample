package com.asml.winetstore.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer {

    private String name;
    private Address billingAddress;
    private Address shippingAddress;
}
