package com.exercise.vendingmachine.service;

import com.exercise.vendingmachine.dto.ProductDto;
import com.exercise.vendingmachine.dto.UserDetailsDto;
import com.exercise.vendingmachine.enumeration.UserRole;
import com.exercise.vendingmachine.model.Product;
import com.exercise.vendingmachine.model.User;
import com.exercise.vendingmachine.repository.ProductRepository;
import com.exercise.vendingmachine.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Mock
    private ProductService productService;

     Product RECORD_1 = new Product(1L,10, 99,"Coca Cola",1001110L);
     Product RECORD_2 = new Product(2L,44, 99,"Fanta",1001110L);

     User RECORD_3 = new User(1001110L,"aydin", "123456", 1L, UserRole.SELLER);

    @Test
    public void getProductById_success() throws Exception{
        given(productRepository.findById(RECORD_1.getId())).willReturn(Optional.of(RECORD_1));

        assertEquals(productServiceImpl.getProduct(1L),RECORD_1);
    }

    @Test
    public void createProductById_success() throws Exception {
        UserDetailsDto userDetailsDto = new UserDetailsDto(RECORD_3);

        ProductDto productDto = new ProductDto();
        productDto.setProductName("Water");
        productDto.setAmountAvailable(30);
        productDto.setCost(100);

        Product record = Product.builder()
                .id(null)
                .amountAvailable(productDto.getAmountAvailable())
                .cost(productDto.getCost())
                .productName(productDto.getProductName())
                .sellerId(userDetailsDto.getUser().getId())
                .build();

        Mockito.when(productRepository.save(record)).thenReturn(record);

        assertEquals(productServiceImpl.createProduct(userDetailsDto, productDto), record);
    }

    @Test
    public void deleteProductById_success() throws Exception {
        UserDetailsDto userDetailsDto = new UserDetailsDto(RECORD_3);

        Mockito.when(productRepository.findByIdAndSellerId(2L, userDetailsDto.getUser().getId()))
                .thenReturn(Optional.ofNullable(RECORD_2));

        assertEquals(productServiceImpl.deleteProduct(userDetailsDto, 2L), RECORD_2);
    }

    @Test
    public void updateProductRecord_success() throws Exception {
        Product record = Product.builder()
                .id(2L)
                .amountAvailable(35)
                .cost(33)
                .productName("Coca Cola")
                .sellerId(1001110L)
                .build();

        UserDetailsDto userDetailsDto = new UserDetailsDto(RECORD_3);

        ProductDto productDto = new ProductDto();
        productDto.setProductName("Coca Cola");
        productDto.setAmountAvailable(20);
        productDto.setCost(50);

        Product updateRecord = Product.builder()
                .id(2L)
                .amountAvailable(20)
                .cost(50)
                .productName("Coca Cola")
                .sellerId(1001110L)
                .build();

        Mockito.when(productRepository.findByIdAndSellerId(2L, userDetailsDto.getUser().getId()))
                .thenReturn(Optional.of(record));
        Mockito.when(productRepository.save(updateRecord)).thenReturn(updateRecord);

        assertEquals(productServiceImpl.updateProduct(userDetailsDto, 2L, productDto), updateRecord);
    }

}
