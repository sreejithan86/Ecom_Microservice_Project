package com.MicroserviceEcom.inventoryservice.service;

import com.MicroserviceEcom.inventoryservice.dto.InventoryResponseDto;
import com.MicroserviceEcom.inventoryservice.model.Inventory;
import com.MicroserviceEcom.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    @Transactional(readOnly = true)
    public List<InventoryResponseDto> isInStock(List<String> skuCode){
       return inventoryRepository.findBySkuCodeIn(skuCode)
               .stream()
               .map(inventory ->
                       InventoryResponseDto.builder()
                               .skuCode(inventory.getSkuCode())
                               .inStock(inventory.getQuantity()>0)
                               .build())
                                .toList();

    }

}
