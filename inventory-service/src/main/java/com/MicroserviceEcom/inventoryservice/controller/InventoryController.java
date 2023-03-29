package com.MicroserviceEcom.inventoryservice.controller;

import com.MicroserviceEcom.inventoryservice.dto.InventoryResponseDto;
import com.MicroserviceEcom.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    @Autowired
    private final InventoryService inventoryService;
    //http://localhost:8082/api/inventory?sku-code=
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponseDto> isInStock(@RequestParam List<String> skuCode){
        return inventoryService.isInStock(skuCode);
    }

}
