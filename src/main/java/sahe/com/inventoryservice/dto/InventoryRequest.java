package sahe.com.inventoryservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be at least 0")
    private Integer quantity;

    @Min(value = 0, message = "Reserved quantity must be at least 0")
    private Integer reservedQuantity = 0;

    @Min(value = 0, message = "Min stock level must be at least 0")
    private Integer minStockLevel = 10;

    @Min(value = 1, message = "Max stock level must be at least 1")
    private Integer maxStockLevel = 1000;

    private String location;
}