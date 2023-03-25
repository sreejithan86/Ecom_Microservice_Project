package com.MicroserviceEcom.productservice.repository;

import com.MicroserviceEcom.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product,String> {
}
