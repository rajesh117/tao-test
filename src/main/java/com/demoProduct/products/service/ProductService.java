package com.demoProduct.products.service;

import com.demoProduct.products.entity.ProductQueueEntity;
import com.demoProduct.products.entity.ProductDTO;
import com.demoProduct.products.entity.ProductEntity;
import com.demoProduct.products.entity.ProductStatus;
import com.demoProduct.products.repository.QueueRepository;
import com.demoProduct.products.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductQueueRepository queueRepository;

    public ResponseEntity<Object> createProduct(ProductDTO product) {
        boolean approvalQueue = false;
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductName(product.getName());
        if(product.getPrice() > 5000){
            productEntity.setProductStatus(ProductStatus.PENDING.getStatusId());
            approvalQueue =true;
        }else{
            productEntity.setProductStatus(ProductStatus.APPROVED.getStatusId());
        }
        productEntity.setPriceRange(product.getPrice());
        productEntity.setProductDateRange(new Date());
        productEntity = productRepository.save(productEntity);
        if(approvalQueue){
            ProductQueueEntity ProductQueueEntity = new ProductQueueEntity();
            ProductQueueEntity.setProductEntity(productEntity);
            ProductQueueEntity.setDateCreated(new Date());
            queueRepository.save(ProductQueueEntity);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(productEntity);

    }

    public List<ProductEntity> getAllActiveProducts(){
        List<ProductEntity> productEntities = productRepository.findAll(Sort.by(Sort.Direction.DESC, "productId"));
        return productEntities.stream().filter(p ->
                p.getProductStatus() == ProductStatus.APPROVED.getStatusId())
                .collect(Collectors.toList());
    }

    public List<ProductEntity> getAllSearchProducts(String productName, Integer minPrice,
                                                    Integer maxPrice, Date minPostedDate,
                                                    Date maxPostedDate){
        List<ProductEntity> productEntities = productRepository.findAll(Sort.by(Sort.Direction.DESC, "productId"));

            return productEntities.stream()
                    .filter(product -> (productName == null || product.getProductName().contains(productName)) &&
                            (minPrice == null || product.getPriceRange() <= minPrice) &&
                            (maxPrice == null || product.getPriceRange() >= maxPrice) &&
                            (minPostedDate == null || product.getProductDateRange().after(minPostedDate)) &&
                            (maxPostedDate == null || product.getProductDateRange().before(maxPostedDate)))
                    .collect(Collectors.toList());


    }

    public ProductEntity updateProduct(long productId, ProductDTO product) {
        boolean approvalQueue = false;
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(()-> new EntityNotFoundException("Product Not found"));
        double previousPrice = productEntity.getPriceRange();
        productEntity.setProductName(product.getName());
        productEntity.setPriceRange(product.getPrice());
        if(product.getPrice() > 1.5 * previousPrice){
            productEntity.setProductStatus(ProductStatus.PENDING.getStatusId());
            approvalQueue =true;
        }else{
            productEntity.setProductStatus(ProductStatus.APPROVED.getStatusId());
        }
        productEntity = productRepository.save(productEntity);
        if(approvalQueue){
            ProductQueueEntity ProductQueueEntity = queueRepository.findByproductEntity(productEntity);
            ProductQueueEntity.setProductEntity(productEntity);
            ProductQueueEntity.setDateCreated(new Date());
            queueRepository.save(ProductQueueEntity);
        }
        return productEntity;
    }

    public String deleteProduct(long productId) {
        try{
            ProductEntity productEntity = productRepository.findById(productId).orElseThrow(
                     ()-> new EntityNotFoundException("Product Not found"));
            queueRepository.deleteByproductEntity(productEntity);
            productRepository.delete(productEntity);
            return  "Product Deleted";
        }catch (Exception e){
            return "Product Deletion Failed : "+e.toString();
        }
    }

    public List<ProductQueueEntity> getAllProductFromApprovalQueue(){
        return queueRepository.findAll()
                .stream().sorted(Comparator.comparing(ProductQueueEntity::getApprovalQueueId).reversed()).toList();
    }

    public ProductEntity approveProductByApprovalId(long approvalId){
        ProductEntity productEntity = queueRepository.findById(approvalId)
                .orElseThrow(()-> new EntityNotFoundException("Product Not found")).getProductEntity();
        if(productEntity.getProductId() > 0 ){
            productEntity.setProductStatus(ProductStatus.APPROVED.getStatusId());
            productRepository.save(productEntity);
            queueRepository.deleteById(approvalId);
        }
        return productEntity;
    }

    public ProductEntity rejectProductByApprovalId(long approvalId){
        ProductEntity productEntity = queueRepository.findById(approvalId)
                .orElseThrow(()-> new EntityNotFoundException("Product Not found")).getProductEntity();
        if(productEntity.getProductId() > 0 ){
            productEntity.setProductStatus(ProductStatus.REJECTED.getStatusId());
            productRepository.save(productEntity);
            queueRepository.deleteById(approvalId);
        }
        return productEntity;
    }
}
