package sahe.com.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sahe.com.inventoryservice.model.Inventory;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductId(Long productId);
    @Query("SELECT i FROM Inventory i WHERE i.quantity <= i.minStockLevel")
    List<Inventory> findLowStockItems();
    @Query("SELECT i FROM Inventory i WHERE i.quantity > i.maxStockLevel")
    List<Inventory> findOverstockedItems();
    List<Inventory> findByLocation(String location);
}
