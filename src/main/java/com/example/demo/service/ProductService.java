package com.example.demo.service;

import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.ProductResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponse::new)
                .toList();
    }

    public List<ProductResponse> getProductsByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .map(ProductResponse::new)
                .toList();
    }

    public ProductResponse getProductById(Long id) {
        return new ProductResponse(findProductById(id));
    }

    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        mapRequestToProduct(request, product);
        return new ProductResponse(productRepository.save(product));
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = findProductById(id);
        mapRequestToProduct(request, product);
        return new ProductResponse(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    private void mapRequestToProduct(ProductRequest request, Product product) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());
    }
}
