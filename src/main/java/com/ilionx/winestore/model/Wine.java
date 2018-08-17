package com.ilionx.winestore.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Wine {

    private String name;
    private String vineyard;
    private Long volume;
    private Double price;
    private Long productionYear;

}
