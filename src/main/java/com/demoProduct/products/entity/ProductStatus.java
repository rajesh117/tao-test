package com.demoProduct.products.entity;


public enum ProductStatus {

    PENDING(1), APPROVED(2), REJECTED(3);

    private int statusId;
    ProductStatus(int statusId) {
        this.statusId = statusId;
    }

    public int getStatusId(){
        return  statusId;
    }

}
