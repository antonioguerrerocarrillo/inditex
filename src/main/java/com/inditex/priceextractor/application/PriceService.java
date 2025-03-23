package com.inditex.priceextractor.application;

import com.inditex.priceextractor.domain.Price;
import com.inditex.priceextractor.domain.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class PriceService {

    private final PriceRepository priceRepository;
    private final SimpleDateFormat simpleDateFormat;

    public PriceService(
            @Autowired PriceRepository priceRepository,
            @Autowired SimpleDateFormat simpleDateFormat
            ) {
        this.priceRepository = priceRepository;
        this.simpleDateFormat = simpleDateFormat;
    }

    public PriceDto getCurrentPrice(GetCurrentPriceRequestDto request) throws ParseException,RuntimeException {

        Date applicationDate =  this.simpleDateFormat.parse(request.applicationDate());
        Optional<Price> priceOpt = this.priceRepository.findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                request.productId(),
                request.brandId(),
                applicationDate,
                applicationDate
        );
        if(priceOpt.isPresent()){
            return PriceDto.fromPrice(this.simpleDateFormat,priceOpt.get());
        }else{
            throw new RuntimeException();
        }
    }
}
