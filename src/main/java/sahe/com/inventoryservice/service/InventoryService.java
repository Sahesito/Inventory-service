package sahe.com.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sahe.com.inventoryservice.client.ProductClient;
import sahe.com.inventoryservice.dto.InventoryRequest;
import sahe.com.inventoryservice.dto.InventoryResponse;
import sahe.com.inventoryservice.client.ProductResponse;
import sahe.com.inventoryservice.dto.StockUpdateRequest;
import sahe.com.inventoryservice.model.Inventory;
import sahe.com.inventoryservice.repository.InventoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductClient productClient;

    public List<InventoryResponse> getAllInventory() {
        log.info("Getting inventory");
        return inventoryRepository.findAll()
                .stream()
                .map(InventoryResponse::new)
                .collect(Collectors.toList());
    }

    public InventoryResponse getInventoryById(Long id) {
        log.info("Obtaining inventory with id: {}", id);
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));
        return new InventoryResponse(inventory);
    }

    public InventoryResponse getInventoryByProductId(Long productId) {
        log.info("Retrieving product from inventory with id: {}", productId);
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
        return new InventoryResponse(inventory);
    }

    public List<InventoryResponse> getLowStockItems() {
        log.info("Getting low stock");
        return inventoryRepository.findLowStockItems()
                .stream()
                .map(InventoryResponse::new)
                .collect(Collectors.toList());
    }

    public List<InventoryResponse> getInventoryByLocation(String location) {
        log.info(": {}", location);
        return inventoryRepository.findByLocation(location)
                .stream()
                .map(InventoryResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public InventoryResponse createInventory(InventoryRequest request) {
        log.info("Creating inventory for product: {}", request.getProductId());
        try {
            ProductResponse product = productClient.getProductById(request.getProductId());
            if (!product.getActive()) {
                throw new RuntimeException("You cannot create an inventory with an inactive product");
            }
        } catch (Exception e) {
            throw new RuntimeException("Product not found with ID: " + request.getProductId());
        }

        if (inventoryRepository.findByProductId(request.getProductId()).isPresent()) {
            throw new RuntimeException("There is already an inventory for this product.: " + request.getProductId());
        }
        Inventory inventory = new Inventory();
        inventory.setProductId(request.getProductId());
        inventory.setQuantity(request.getQuantity());
        inventory.setReservedQuantity(request.getReservedQuantity());
        inventory.setMinStockLevel(request.getMinStockLevel());
        inventory.setMaxStockLevel(request.getMaxStockLevel());
        inventory.setLocation(request.getLocation());
        Inventory savedInventory = inventoryRepository.save(inventory);
        log.info("Inventory created successfully with id: {}", savedInventory.getId());
        return new InventoryResponse(savedInventory);
    }

    @Transactional
    public InventoryResponse updateInventory(Long id, InventoryRequest request) {
        log.info("Updating inventory with id: {}", id);
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));
        inventory.setQuantity(request.getQuantity());
        inventory.setReservedQuantity(request.getReservedQuantity());
        inventory.setMinStockLevel(request.getMinStockLevel());
        inventory.setMaxStockLevel(request.getMaxStockLevel());
        inventory.setLocation(request.getLocation());
        Inventory updatedInventory = inventoryRepository.save(inventory);
        log.info("Inventory updated correctly");
        return new InventoryResponse(updatedInventory);
    }

    @Transactional
    public InventoryResponse addStock(Long id, StockUpdateRequest request) {
        log.info("Adding {} units to inventory, id: {}", request.getQuantity(), id);
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));
        inventory.setQuantity(inventory.getQuantity() + request.getQuantity());
        Inventory updatedInventory = inventoryRepository.save(inventory);
        log.info("Stock added successfully. New quantity: {}", updatedInventory.getQuantity());
        return new InventoryResponse(updatedInventory);
    }

    @Transactional
    public InventoryResponse reduceStock(Long id, StockUpdateRequest request) {
        log.info("Reducing {} units to inventory, id: {}", request.getQuantity(), id);
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));
        if (!inventory.canFulfill(request.getQuantity())) {
            throw new RuntimeException("Insufficient stock. Available: " + inventory.getAvailableQuantity()
                    + ", Required: " + request.getQuantity());
        }
        inventory.setQuantity(inventory.getQuantity() - request.getQuantity());
        Inventory updatedInventory = inventoryRepository.save(inventory);
        log.info("Stock reduced correctly. New quantity.: {}", updatedInventory.getQuantity());
        return new InventoryResponse(updatedInventory);
    }

    @Transactional
    public void deleteInventory(Long id) {
        log.info("Deleting inventory with id: {}", id);
        if (!inventoryRepository.existsById(id)) {
            throw new RuntimeException("Inventory not found with id: " + id);
        }
        inventoryRepository.deleteById(id);
        log.info("Inventory successfully deleted");
    }

    public boolean checkAvailability(Long productId, Integer quantity) {
        log.info("Checking product availability: {}, amount: {}", productId, quantity);
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product inventory not found: " + productId));
        return inventory.canFulfill(quantity);
    }
}
