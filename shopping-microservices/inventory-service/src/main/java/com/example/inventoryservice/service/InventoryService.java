package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    @Transactional(readOnly = true)
    public List<InventoryResponse> isinstock(List<String> SkuCode){
        return inventoryRepository.findBySkuCodeIn(SkuCode).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .isinstock(inventory.getQuantity()>0)
                                .skucode(inventory.getSkuCode())
                                .build()

                ).toList();
    }
}
