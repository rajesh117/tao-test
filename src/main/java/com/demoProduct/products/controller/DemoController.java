package com.demoproduct.products.controller;

import com.demoproduct.products.entity.ProductDTO;
import com.demoproduct.products.entity.ProductEntity;
import com.demoproduct.products.service.ProductService;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {


    @Autowired
    private ProductService ps;

    @GetMapping
    public List<ProductEntity> getProducts(){
        return ps.getAllActiveProducts();
    }

    @GetMapping("/search")
    public List<ProductEntity> getSearchProducts(
            @RequestParam(value = "productName", required=false) String productName,
            @RequestParam(value = "minPrice", required=false) Integer minPrice,
            @RequestParam(value = "maxPrice", required=false) Integer maxPrice,
            @ApiParam(value = "Date in 'yyyy-MM-dd' format")
            @RequestParam(value = "minPostedDate", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date minPostedDate,
            @ApiParam(value = "Date in 'yyyy-MM-dd' format")
            @RequestParam(value = "maxPostedDate", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date maxPostedDate){


        return ps.getAllSearchProducts(productName, minPrice, maxPrice, minPostedDate, maxPostedDate);
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody ProductDTO product) {

        if(product.getPrice() > 10000){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Product price should not be more than $10000.");
        }else{
            return ps.createProduct(product);
        }

    }

    @PutMapping("/{productId}")
    public ResponseEntity<Object> updateProduct(@PathVariable("productId") long pId, @RequestBody ProductDTO product) {

        if(product.getPrice() > 10000){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Product price should not be more $10000.");
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(ps.updateProduct(pId, product));
        }

    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("productId") long pId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ps.deleteProduct(pId));
    }

    @GetMapping("/approval-queue")
    public ResponseEntity<Object> getAllProductFromApprovalQueue(){
        return  ResponseEntity.status(HttpStatus.OK).body(ps.getAllProductFromApprovalQueue());
    }

   @PutMapping("/approval-queue/{aId}/approve")
    public ResponseEntity<Object> approveProductByApprovalId(@PathVariable("aId") long aId){
        return  ResponseEntity.status(HttpStatus.OK).body(ps.approveProductByApprovalId(aId));
    }

    @PutMapping("/approval-queue/{aId}/reject")
    public ResponseEntity<Object> rejectProductByApprovalId(@PathVariable("aId") long aId){
        return  ResponseEntity.status(HttpStatus.OK).body(ps.rejectProductByApprovalId(aId));
    }
}
