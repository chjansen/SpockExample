package nl.codeclan.basiccrudaplication.controller;

import nl.codeclan.basiccrudaplication.dto.ProductRequest;
import nl.codeclan.basiccrudaplication.model.Product;
import nl.codeclan.basiccrudaplication.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> list() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> get(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductRequest req) {
        OffsetDateTime now = OffsetDateTime.now();
        Product p = Product.builder()
                .sku(req.getSku())
                .name(req.getName())
                .description(req.getDescription())
                .unitPriceCents(req.getUnitPriceCents())
                .active(req.getActive() == null ? Boolean.TRUE : req.getActive())
                .createdAt(now)
                .updatedAt(now)
                .build();
        Product saved = productRepository.save(p);
        return ResponseEntity.created(URI.create("/api/products/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody ProductRequest req) {
        return productRepository.findById(id)
                .map(existing -> {
                    if (req.getSku() != null) existing.setSku(req.getSku());
                    if (req.getName() != null) existing.setName(req.getName());
                    if (req.getDescription() != null) existing.setDescription(req.getDescription());
                    if (req.getUnitPriceCents() != null) existing.setUnitPriceCents(req.getUnitPriceCents());
                    if (req.getActive() != null) existing.setActive(req.getActive());
                    existing.setUpdatedAt(OffsetDateTime.now());
                    return ResponseEntity.ok(productRepository.save(existing));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
