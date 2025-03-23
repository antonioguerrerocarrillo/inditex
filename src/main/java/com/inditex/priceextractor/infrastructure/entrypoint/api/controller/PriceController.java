package com.inditex.priceextractor.infrastructure.entrypoint.api.controller;

import com.inditex.priceextractor.application.GetCurrentPriceRequestDto;
import com.inditex.priceextractor.application.PriceDto;
import com.inditex.priceextractor.application.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;

@Controller
public class PriceController {

    private final PriceService priceService;

    public PriceController(@Autowired PriceService $priceService) {
        this.priceService = $priceService;
    }

    @GetMapping("/price")
    @ResponseBody
    public ResponseEntity<PriceDto> getPrice(
            @RequestParam("application_date") String applicationDate,
            @RequestParam("product_id") long productId,
            @RequestParam("brand_id") long brandId
    ) {

        try {
            return ResponseEntity.ok()
                    .body(
                            this.priceService.getCurrentPrice(
                                    new GetCurrentPriceRequestDto(applicationDate, productId, brandId)
                            )
                    );
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
