package com.myServices.inventoryService;

import com.myServices.inventoryService.model.Inventory;
import com.myServices.inventoryService.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner loadData(InventoryRepository inventoryRepository)
//	{
//		return args -> {
//			Inventory inventory=new Inventory();
//			inventory.setSkuCode("iphone_13");
//			inventory.setQuantity(100);
//			Inventory inventory1=new Inventory();
//			inventory1.setSkuCode("11_pro_max");
//			inventory1.setQuantity(0);
//			inventoryRepository.save(inventory);
//			inventoryRepository.save(inventory1);
//		};
//	}

}
