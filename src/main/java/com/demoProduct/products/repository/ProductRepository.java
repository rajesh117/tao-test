package com.demoProduct.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.products.entity.ProductEntity;


@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {


    public ProductEntity findByProductNameAndPriceRange(String productName, int priceRange);
}
