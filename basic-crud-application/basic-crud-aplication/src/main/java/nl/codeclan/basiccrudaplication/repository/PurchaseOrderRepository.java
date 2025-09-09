package nl.codeclan.basiccrudaplication.repository;

import nl.codeclan.basiccrudaplication.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
}
