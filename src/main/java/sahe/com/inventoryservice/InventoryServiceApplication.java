package sahe.com.inventoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
        System.out.println("""
                INVENTORY SERVICE - RUNNING
                Port: 8084
                GET /inventory - Get all inventory
                GET /inventory/{id} - Get by id
                GET /inventory/product/{id} - Get by product
                GET /inventory/low-stock - Low stock items
                POST /inventory - Create inventory
                PATCH /inventory/{id}/add-stock - Add stock
                """);
    }

}
