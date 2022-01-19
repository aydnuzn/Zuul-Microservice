package com.exercise.vendingmachine.service;

import com.exercise.vendingmachine.dto.BuyResponseDto;
import com.exercise.vendingmachine.dto.DepositDto;
import com.exercise.vendingmachine.dto.PurchaseDto;
import com.exercise.vendingmachine.dto.UserDetailsDto;
import com.exercise.vendingmachine.enumeration.CoinEnum;
import com.exercise.vendingmachine.enumeration.UserRole;
import com.exercise.vendingmachine.model.Product;
import com.exercise.vendingmachine.model.Purchase;
import com.exercise.vendingmachine.model.User;
import com.exercise.vendingmachine.repository.ProductRepository;
import com.exercise.vendingmachine.repository.PurchaseRepository;
import com.exercise.vendingmachine.repository.UserRepository;
import com.exercise.vendingmachine.service.impl.VendingMachineServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class VendingMachineServiceTest {

    @InjectMocks
    private VendingMachineServiceImpl vendingMachineServiceImpl;

    @Mock
    private VendingMachineService vendingMachineService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    User RECORD_1 = new User(1L,"aydin", "123456", 120L, UserRole.BUYER);
    Product RECORD_2 = new Product(1L,10, 20,"Coca Cola",1001110L);

    @Test
    public void reset_success(){
        UserDetailsDto userDetailsDto = new UserDetailsDto(RECORD_1);

        User user = RECORD_1;
        user.setDeposit(0L);

        Mockito.when(userRepository.findById(userDetailsDto.getUser().getId())).thenReturn(Optional.of(RECORD_1));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        assertEquals(vendingMachineServiceImpl.reset(userDetailsDto), user);
    }

    @Test
    public void deposit_success(){
        UserDetailsDto userDetailsDto = new UserDetailsDto(RECORD_1);

        DepositDto depositDto = new DepositDto();
        depositDto.setCoin(CoinEnum.CENTS_10);

        User user = RECORD_1;
        user.setDeposit(user.getDeposit() + depositDto.getCoin().getCents());

        Mockito.when(userRepository.findById(userDetailsDto.getUser().getId()))
                .thenReturn(Optional.of(RECORD_1));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        assertEquals(vendingMachineServiceImpl.deposit(userDetailsDto, depositDto), user);
    }

    @Test
    public void buy_success(){
        UserDetailsDto userDetailsDto = new UserDetailsDto(RECORD_1);

        PurchaseDto purchaseDto = new PurchaseDto();
        purchaseDto.setProductId(RECORD_2.getId());
        purchaseDto.setAmount(2);

        Long oldDeposit = RECORD_1.getDeposit();
        Long totalCost = Long.valueOf(RECORD_2.getCost() * purchaseDto.getAmount());

        User user = RECORD_1;
        user.setDeposit(RECORD_1.getDeposit() - purchaseDto.getAmount() * RECORD_2.getCost());

        Product product = RECORD_2;
        product.setAmountAvailable(RECORD_2.getAmountAvailable() - purchaseDto.getAmount());

        Mockito.when(productRepository.findById(purchaseDto.getProductId()))
                .thenReturn(Optional.of(RECORD_2));
        Mockito.when(userRepository.findById(userDetailsDto.getUser().getId()))
                .thenReturn(Optional.of(RECORD_1));
        Mockito.when(userRepository.saveAndFlush(user)).thenReturn(user);
        Mockito.when(productRepository.saveAndFlush(product)).thenReturn(product);

        Purchase purchase = Purchase.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .productId(product.getId())
                .unitCost(product.getCost())
                .productName(product.getProductName())
                .sellerId(product.getSellerId())
                .purchaseAmount(purchaseDto.getAmount())
                .totalCost(totalCost)
                .oldDeposit(oldDeposit)
                .newDeposit(user.getDeposit())
                .build();

        List<Purchase> purchases = new ArrayList<>();
        purchases.add(purchase);

        Mockito.when(purchaseRepository.findByUserId(user.getId()))
                .thenReturn(purchases);

        long totalSpent = purchases.stream().mapToLong(Purchase::getTotalCost).sum();

        BuyResponseDto buyResponseDto = BuyResponseDto.builder()
                .totalSpent(totalSpent)
                .purchases(purchases)
                .build();

        assertEquals(vendingMachineServiceImpl.buy(userDetailsDto, purchaseDto), buyResponseDto);
    }

}
