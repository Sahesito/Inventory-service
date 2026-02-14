package sahe.com.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sahe.com.inventoryservice.model.Inventory;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {

    private Long id;
    private Long productId;
    private Integer quantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;
    private Integer minStockLevel;
    private Integer maxStockLevel;
    private String location;
    private Boolean lowStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public InventoryResponse(Inventory inventory) {
        this.id = inventory.getId();
        this.productId = inventory.getProductId();
        this.quantity = inventory.getQuantity();
        this.reservedQuantity = inventory.getReservedQuantity();
        this.availableQuantity = inventory.getAvailableQuantity();
        this.minStockLevel = inventory.getMinStockLevel();
        this.maxStockLevel = inventory.getMaxStockLevel();
        this.location = inventory.getLocation();
        this.lowStock = inventory.isLowStock();
        this.createdAt = inventory.getCreatedAt();
        this.updatedAt = inventory.getUpdatedAt();
    }
}