package sahe.com.inventoryservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sahe.com.inventoryservice.dto.InventoryRequest;
import sahe.com.inventoryservice.dto.InventoryResponse;
import sahe.com.inventoryservice.dto.StockUpdateRequest;
import sahe.com.inventoryservice.model.Inventory;
import sahe.com.inventoryservice.repository.InventoryRepository;
import sahe.com.inventoryservice.service.InventoryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryRepository inventoryRepository;

    // GET http://localhost:8084/inventory
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {
        log.info("GET /inventory - Get all inventory");
        List<InventoryResponse> inventory = inventoryService.getAllInventory();
        return ResponseEntity.ok(inventory);
    }

    // GET http://localhost:8084/inventory/1
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<InventoryResponse> getInventoryById(@PathVariable Long id) {
        log.info("GET /inventory/{} - Get inventory by id", id);
        InventoryResponse inventory = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(inventory);
    }

    // GET http://localhost:8084/inventory/product/1
    @GetMapping("/product/{productId}")
    public ResponseEntity<InventoryResponse> getInventoryByProductId(@PathVariable Long productId) {
        log.info("GET /inventory/product/{} - Get inventory by product id", productId);
        InventoryResponse inventory = inventoryService.getInventoryByProductId(productId);
        return ResponseEntity.ok(inventory);
    }

    // GET http://localhost:8084/inventory/low-stock
    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<List<InventoryResponse>> getLowStockItems() {
        log.info("GET /inventory/low-stock - Get low stock items");
        List<InventoryResponse> lowStock = inventoryService.getLowStockItems();
        return ResponseEntity.ok(lowStock);
    }

    // GET http://localhost:8084/inventory/location/Warehouse-A
    @GetMapping("/location/{location}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<List<InventoryResponse>> getInventoryByLocation(@PathVariable String location) {
        log.info("GET /inventory/location/{} - Get inventory by location", location);
        List<InventoryResponse> inventory = inventoryService.getInventoryByLocation(location);
        return ResponseEntity.ok(inventory);
    }

    // GET http://localhost:8084/inventory/check-availability?productId=1&quantity=5
    @GetMapping("/check-availability")
    public ResponseEntity<Map<String, Object>> checkAvailability(
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        log.info("GET /inventory/check-availability - Product: {}, Quantity: {}", productId, quantity);
        boolean available = inventoryService.checkAvailability(productId, quantity);
        Map<String, Object> response = new HashMap<>();
        response.put("productId", productId);
        response.put("requestedQuantity", quantity);
        response.put("available", available);
        return ResponseEntity.ok(response);
    }

    // POST http://localhost:8084/inventory
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<InventoryResponse> createInventory(@Valid @RequestBody InventoryRequest request) {
        log.info("POST /inventory - Create inventory for product: {}", request.getProductId());
        InventoryResponse createdInventory = inventoryService.createInventory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInventory);
    }

    // PUT http://localhost:8084/inventory/1
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<InventoryResponse> updateInventory(
            @PathVariable Long id,
            @Valid @RequestBody InventoryRequest request) {
        log.info("PUT /inventory/{} - Update inventory", id);
        InventoryResponse updatedInventory = inventoryService.updateInventory(id, request);
        return ResponseEntity.ok(updatedInventory);
    }

    // PATCH http://localhost:8084/inventory/1/add-stock
    @PatchMapping("/{id}/add-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<InventoryResponse> addStock(
            @PathVariable Long id,
            @Valid @RequestBody StockUpdateRequest request) {
        log.info("PATCH /inventory/{}/add-stock - Add {} units", id, request.getQuantity());
        InventoryResponse updatedInventory = inventoryService.addStock(id, request);
        return ResponseEntity.ok(updatedInventory);
    }

    // PATCH http://localhost:8084/inventory/1/reduce-stock
    @PatchMapping("/{id}/reduce-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<InventoryResponse> reduceStock(
            @PathVariable Long id,
            @Valid @RequestBody StockUpdateRequest request) {
        log.info("PATCH /inventory/{}/reduce-stock - Reduce {} units", id, request.getQuantity());
        InventoryResponse updatedInventory = inventoryService.reduceStock(id, request);
        return ResponseEntity.ok(updatedInventory);
    }

    // DELETE http://localhost:8084/inventory/1
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        log.info("DELETE /inventory/{} - Delete inventory", id);
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }


    // POST http://localhost:8084/inventory/product/{productId}/reduce-stock
    @PostMapping("/product/{productId}/reduce-stock")
    public ResponseEntity<InventoryResponse> reduceStockByProductId(
            @PathVariable Long productId,
            @Valid @RequestBody StockUpdateRequest request) {
        log.info("POST /inventory/product/{}/reduce-stock - Reduce {} units", productId, request.getQuantity());

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product id: " + productId));

        InventoryResponse updatedInventory = inventoryService.reduceStock(inventory.getId(), request);

        log.info("Stock reduced successfully for product: {}", productId);
        return ResponseEntity.ok(updatedInventory);
    }
}
