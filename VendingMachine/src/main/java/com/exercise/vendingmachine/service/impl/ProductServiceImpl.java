package com.exercise.vendingmachine.service.impl;

import com.exercise.vendingmachine.dto.ProductDto;
import com.exercise.vendingmachine.dto.UserDetailsDto;
import com.exercise.vendingmachine.exception.GlobalExceptionHandler;
import com.exercise.vendingmachine.model.Product;
import com.exercise.vendingmachine.repository.ProductRepository;
import com.exercise.vendingmachine.service.ProductService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = Logger.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(UserDetailsDto userDetailsDto, ProductDto productDto) {
        Product product = Product.builder()
                .amountAvailable(productDto.getAmountAvailable())
                .cost(productDto.getCost())
                .productName(productDto.getProductName())
                .sellerId(userDetailsDto.getUser().getId())
                .build();
        product = productRepository.save(product);
        LOG.info("Product Created");
        return product;
    }

    /*
     * Get product can be called by both (all) seller or buyer accounts
     */
    @Override
    public Product getProduct(Long productId) {
        List<Product> productList = productRepository.findAll();

        Optional<Product> optionalProduct = productRepository.findById(productId);
        Product product = optionalProduct.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        LOG.info("Product Fetch Successfully.");
        return product;
    }

    @Override
    @Transactional
    public Product updateProduct(UserDetailsDto userDetailsDto, Long productId, ProductDto productDto) {
        Product product = this.productRepository.findByIdAndSellerId(productId, userDetailsDto.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        product.setAmountAvailable(productDto.getAmountAvailable());
        product.setCost(productDto.getCost());
        product.setProductName(productDto.getProductName());
        product = this.productRepository.saveAndFlush(product);
        LOG.info("Product Updated");
        return product;
    }

    @Override
    @Transactional
    public Product deleteProduct(UserDetailsDto userDetailsDto, Long productId) {
        Product product = this.productRepository.findByIdAndSellerId(productId, userDetailsDto.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        this.productRepository.delete(product);
        LOG.info("Product Deleted");
        return product;
    }

}
