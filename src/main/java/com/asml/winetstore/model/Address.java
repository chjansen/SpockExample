package com.asml.winetstore.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {

    private String street;
    private Long number;
    private String postfix;
    private String zipCode;
    private String city;
}
