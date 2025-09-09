package nl.codeclan.basiccrudaplication.dto;

import lombok.Data;

@Data
public class ProductRequest {
    private String sku;
    private String name;
    private String description;
    private Long unitPriceCents;
    private Boolean active;
}
