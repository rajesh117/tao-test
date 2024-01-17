# productscreen

This Project is Product Screening, For adding, updating, filtering, deleting and approving or rejecting products.
Having these services:
1. API to List Active Products:
   1. Endpoint: GET /api/products 
   2. Description: Get the list of active products sorted by the latest first.
2. API to Search Products:
   1. Endpoint: GET /api/products/search
   2. Parameters: productName (optional), minPrice (optional), maxPrice (optional),
      minPostedDate (optional), maxPostedDate (optional)
   3. Description: Search for products based on the given criteria (product name, price range,
      and posted date range).
3. API to Create a Product:
   1. Endpoint: POST /api/products 
   2. Request Body: Product object (name, price, status)
   3. Description: Create a new product, but the price must not exceed $10,000. If the price is
      more than $5,000, the product should be pushed to the approval queue.

4. API to Update a Product:
   1. Endpoint: PUT /api/products/{productId} 
   2. Request Body: Product object (name, price, status)
   3. Description: Update an existing product, but if the price is more than 50% of its previous
      price, the product should be pushed to the approval queue.

5. API to Delete a Product:
   1. Endpoint: DELETE /api/products/{productId} 
   2. Description: Delete a product, and it should be pushed to the approval queue.
6. API to Get Products in Approval Queue:
   1. Endpoint: GET /api/products/approval-queue
   2. Description: Get all the products in the approval queue, sorted by request date (earliest
   first).

7. API to Approve a Product:
   1. Endpoint: PUT /api/products/approval-queue/{approvalId}/approve 
   2. Description: Approve a product from the approval queue. The product state should be
updated, and it should no longer appear in the approval queue.

8. API to Reject a Product:
   1. Endpoint: PUT /api/products/approval-queue/{approvalId}/reject
   2. Description: Reject a product from the approval queue. The product state should remain
   the same, and it should no longer appear in the approval queue.


Status is mentioned with PENDING(1), APPROVED(2), REJECTED(3)

Having the swagger ui for testing the API services:
http://localhost:8080/swagger-ui.html
