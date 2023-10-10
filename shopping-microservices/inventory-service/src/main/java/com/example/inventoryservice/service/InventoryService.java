package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> isinstock(List<String> SkuCode){
      log.info("wait started");
      Thread.sleep(10000);
      log.info("ended");
        return inventoryRepository.findBySkuCodeIn(SkuCode).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .isinstock(inventory.getQuantity()>0)
                                .skucode(inventory.getSkuCode())
                                .build()

                ).toList();
    }
}
