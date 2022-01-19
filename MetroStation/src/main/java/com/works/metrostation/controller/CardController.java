package com.works.metrostation.controller;

import com.works.metrostation.dto.CardDto;
import com.works.metrostation.dto.CardMoneyDto;
import com.works.metrostation.dto.UserDetailsDto;
import com.works.metrostation.model.Card;
import com.works.metrostation.service.CardService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(
        value = "/metro/cards",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    private Card createCard(@RequestBody @Valid CardDto cardDto){
        return cardService.createCard(cardDto);
    }

    @GetMapping("/{cardNumber}")
    private Card getCard(@AuthenticationPrincipal UserDetailsDto userDetailsDto, @PathVariable String cardNumber){
        return cardService.getCard(userDetailsDto, cardNumber);
    }

    @PutMapping
    private Card loadingMoneyOnCard(@RequestBody CardMoneyDto cardMoneyDto){
        return cardService.loadingMoneyOnCard(cardMoneyDto);
    }

    @DeleteMapping(value = "/{cardNumber}", consumes = MediaType.ALL_VALUE)
    private Card deleteCard(@PathVariable String cardNumber){
        return cardService.deleteCard(cardNumber);
    }

}
