package com.demoProduct.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.products.entity.ProductQueueEntity;
import com.product.products.entity.ProductEntity;

@Repository
public interface ProductQueueRepository extends JpaRepository<ProductQueueEntity, Long> {

    public ProductQueueEntity findByproductEntity(ProductEntity productEntity);

    public ProductQueueEntity deleteByproductEntity(ProductEntity productEntity);
}
