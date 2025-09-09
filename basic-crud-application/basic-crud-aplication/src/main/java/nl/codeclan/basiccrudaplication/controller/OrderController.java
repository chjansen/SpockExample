package nl.codeclan.basiccrudaplication.controller;

import nl.codeclan.basiccrudaplication.dto.OrderItemRequest;
import nl.codeclan.basiccrudaplication.dto.OrderRequest;
import nl.codeclan.basiccrudaplication.model.OrderItem;
import nl.codeclan.basiccrudaplication.model.Product;
import nl.codeclan.basiccrudaplication.model.PurchaseOrder;
import nl.codeclan.basiccrudaplication.repository.ProductRepository;
import nl.codeclan.basiccrudaplication.repository.PurchaseOrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final PurchaseOrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderController(PurchaseOrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<PurchaseOrder> list() {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrder> get(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<PurchaseOrder> create(@RequestBody OrderRequest req) {
        OffsetDateTime now = OffsetDateTime.now();
        PurchaseOrder order = PurchaseOrder.builder()
                .customerName(req.getCustomerName())
                .status("NEW")
                .createdAt(now)
                .updatedAt(now)
                .build();

        if (req.getItems() != null) {
            for (OrderItemRequest itemReq : req.getItems()) {
                Product product = productRepository.findById(itemReq.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Product not found: " + itemReq.getProductId()));
                OrderItem item = OrderItem.builder()
                        .order(order)
                        .product(product)
                        .quantity(itemReq.getQuantity())
                        .unitPriceCents(product.getUnitPriceCents())
                        .build();
                order.getItems().add(item);
            }
        }

        PurchaseOrder saved = orderRepository.save(order);
        return ResponseEntity.created(URI.create("/api/orders/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<PurchaseOrder> update(@PathVariable Long id, @RequestBody OrderRequest req) {
        return orderRepository.findById(id)
                .map(order -> {
                    if (req.getCustomerName() != null) order.setCustomerName(req.getCustomerName());
                    order.setUpdatedAt(OffsetDateTime.now());

                    if (req.getItems() != null) {
                        order.getItems().clear();
                        for (OrderItemRequest itemReq : req.getItems()) {
                            Product product = productRepository.findById(itemReq.getProductId())
                                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + itemReq.getProductId()));
                            OrderItem item = OrderItem.builder()
                                    .order(order)
                                    .product(product)
                                    .quantity(itemReq.getQuantity())
                                    .unitPriceCents(product.getUnitPriceCents())
                                    .build();
                            order.getItems().add(item);
                        }
                    }

                    PurchaseOrder saved = orderRepository.save(order);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        orderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
