package com.asml.winetstore.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderLine {

    private Wine wine;
    private String description;
    private Long number;
    private Double total;
}
