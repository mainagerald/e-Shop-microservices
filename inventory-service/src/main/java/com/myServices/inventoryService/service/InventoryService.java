package com.myServices.inventoryService.service;

import com.myServices.inventoryService.dto.InventoryResponse;
import com.myServices.inventoryService.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCodes) {
        // Find the inventory items matching the provided skuCodes
        List<InventoryResponse> responses = inventoryRepository.findBySkuCodeIn(skuCodes)
                .stream()
                // Map each inventory item to an InventoryResponse object
                .map(inventory -> InventoryResponse.builder()
                        .skuCode(inventory.getSkuCode())
                        .isInStock(inventory.getQuantity() > 0) // Set isInStock based on the quantity
                        .build())
                .toList(); // Convert the stream to a list

        // Extract the skuCodes of the found inventory items
        List<String> foundSkuCodes = responses.stream()
                .map(InventoryResponse::getSkuCode)
                .toList();

        // Find the skuCodes that were not found in the inventory
        List<InventoryResponse> missingSkuCodesResponses = skuCodes.stream()
                .filter(skuCode -> !foundSkuCodes.contains(skuCode)) // Filter out found skuCodes
                // Create an InventoryResponse object for each missing skuCode with isInStock set to false
                .map(skuCode -> InventoryResponse.builder()
                        .skuCode(skuCode)
                        .isInStock(false)
                        .build())
                .toList();

        // Create a new mutable list to hold all responses
        List<InventoryResponse> allResponses = new ArrayList<>(responses);
        // Add the missing skuCodes responses to the list
        allResponses.addAll(missingSkuCodesResponses);

        // Return the list containing all responses
        return allResponses;
    }
}
