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

    // Obtener Inventario
    public List<InventoryResponse> getAllInventory() {
        log.info("Obteniendo el inventario");
        return inventoryRepository.findAll()
                .stream()
                .map(InventoryResponse::new)
                .collect(Collectors.toList());
    }

    // Obtener x id
    public InventoryResponse getInventoryById(Long id) {
        log.info("Obteniendo inventario con id: {}", id);
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con id: " + id));
        return new InventoryResponse(inventory);
    }

    // Obtener producto x id
    public InventoryResponse getInventoryByProductId(Long productId) {
        log.info("Obteniendo producto del inventario con id: {}", productId);
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + productId));
        return new InventoryResponse(inventory);
    }

    // Obtener productos con stock bajo
    public List<InventoryResponse> getLowStockItems() {
        log.info("Obteniendo bajo stock");
        return inventoryRepository.findLowStockItems()
                .stream()
                .map(InventoryResponse::new)
                .collect(Collectors.toList());
    }

    // Obtener por ubicacion
    public List<InventoryResponse> getInventoryByLocation(String location) {
        log.info("Obteniendo inventario por ubicacion: {}", location);
        return inventoryRepository.findByLocation(location)
                .stream()
                .map(InventoryResponse::new)
                .collect(Collectors.toList());
    }

    // Crear inventario
    @Transactional
    public InventoryResponse createInventory(InventoryRequest request) {
        log.info("Creando inventario para producto: {}", request.getProductId());

        // Verificar que el producto existe
        try {
            ProductResponse product = productClient.getProductById(request.getProductId());
            if (!product.getActive()) {
                throw new RuntimeException("No se puede crear un inventario con un producto inactivo");
            }
        } catch (Exception e) {
            throw new RuntimeException("Producto no encontrado con id: " + request.getProductId());
        }

        // Verificar que no exista inventario para ese producto
        if (inventoryRepository.findByProductId(request.getProductId()).isPresent()) {
            throw new RuntimeException("Ya existe un inventario para este producto: " + request.getProductId());
        }
        Inventory inventory = new Inventory();
        inventory.setProductId(request.getProductId());
        inventory.setQuantity(request.getQuantity());
        inventory.setReservedQuantity(request.getReservedQuantity());
        inventory.setMinStockLevel(request.getMinStockLevel());
        inventory.setMaxStockLevel(request.getMaxStockLevel());
        inventory.setLocation(request.getLocation());
        Inventory savedInventory = inventoryRepository.save(inventory);
        log.info("Inventario creado correctamente con id: {}", savedInventory.getId());
        return new InventoryResponse(savedInventory);
    }

    // Actualizar inventario
    @Transactional
    public InventoryResponse updateInventory(Long id, InventoryRequest request) {
        log.info("Actualizando inventario con id: {}", id);
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con id: " + id));
        inventory.setQuantity(request.getQuantity());
        inventory.setReservedQuantity(request.getReservedQuantity());
        inventory.setMinStockLevel(request.getMinStockLevel());
        inventory.setMaxStockLevel(request.getMaxStockLevel());
        inventory.setLocation(request.getLocation());
        Inventory updatedInventory = inventoryRepository.save(inventory);
        log.info("Inventario actualizado correctamente");
        return new InventoryResponse(updatedInventory);
    }

    // Agregar Stock
    @Transactional
    public InventoryResponse addStock(Long id, StockUpdateRequest request) {
        log.info("Agregando {} unidades al inventario, id: {}", request.getQuantity(), id);
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con id: " + id));
        inventory.setQuantity(inventory.getQuantity() + request.getQuantity());
        Inventory updatedInventory = inventoryRepository.save(inventory);
        log.info("Stock agregado correctamente. Nueva cantidad: {}", updatedInventory.getQuantity());
        return new InventoryResponse(updatedInventory);
    }

    // Reducir stock
    @Transactional
    public InventoryResponse reduceStock(Long id, StockUpdateRequest request) {
        log.info("Reduciendo {} unidades del inventario, id: {}", request.getQuantity(), id);
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con id: " + id));
        if (!inventory.canFulfill(request.getQuantity())) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + inventory.getAvailableQuantity()
                    + ", Solicitado: " + request.getQuantity());
        }
        inventory.setQuantity(inventory.getQuantity() - request.getQuantity());
        Inventory updatedInventory = inventoryRepository.save(inventory);
        log.info("Stock reducido correctamente. Nueva cantidad: {}", updatedInventory.getQuantity());
        return new InventoryResponse(updatedInventory);
    }

    // Eliminar inventario
    @Transactional
    public void deleteInventory(Long id) {
        log.info("Eliminando inventario con id: {}", id);
        if (!inventoryRepository.existsById(id)) {
            throw new RuntimeException("Inventario no encontrado con id: " + id);
        }
        inventoryRepository.deleteById(id);
        log.info("Inventario eliminado correctamente");
    }

    // Verificar Disponibilidad
    public boolean checkAvailability(Long productId, Integer quantity) {
        log.info("Revisando disponibilidad de producto: {}, cantidad: {}", productId, quantity);
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado de producto: " + productId));
        return inventory.canFulfill(quantity);
    }
}
