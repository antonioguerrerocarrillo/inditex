package com.inditex.priceextractor.application;

import com.inditex.priceextractor.domain.Price;
import com.inditex.priceextractor.domain.PriceRepository;
import com.inditex.priceextractor.infrastructure.format.date.SimpleDateFormatConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PriceServiceUnitTest {
    AutoCloseable openMocks;
    @Mock
    PriceRepository priceRepositoryMock;

    SimpleDateFormat simpleDateFormat;
    PriceService priceService;

    @BeforeEach
    public void setup() {
        this.openMocks = MockitoAnnotations.openMocks(this);
        this.simpleDateFormat = new SimpleDateFormatConfig().simpleDateFormat();
        this.priceService = new PriceService(priceRepositoryMock, simpleDateFormat);
    }

    @AfterEach
    void tearDown() throws Exception {
        this.openMocks.close();
    }

    @Test
    public void givenValidRequest_thenValidPriceResponseDtoReturned() throws ParseException {
        GetCurrentPriceRequestDto givenRequest = new GetCurrentPriceRequestDto(
                "2020-06-14-10.00.00",
                35455,
                1
        );

        Price expectedPrice = this.creteExpectedPrice(givenRequest);

        Date givenApplicationDate = this.simpleDateFormat.parse(givenRequest.applicationDate());

        when(
                this.priceRepositoryMock.findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                        givenRequest.productId(),
                        givenRequest.brandId(),
                        givenApplicationDate,
                        givenApplicationDate
                )
        ).thenReturn(Optional.of(expectedPrice));

        PriceDto priceResponseDto = this.priceService.getCurrentPrice(givenRequest);

        verify(
                this.priceRepositoryMock,
                times(1)
        ).findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                givenRequest.productId(),
                givenRequest.brandId(),
                givenApplicationDate,
                givenApplicationDate
        );

        Assertions.assertEquals(PriceDto.fromPrice(this.simpleDateFormat, expectedPrice), priceResponseDto);

    }

    private Price creteExpectedPrice(GetCurrentPriceRequestDto givenRequest) throws ParseException {
        return new Price(
                1,
                givenRequest.brandId(),
                this.simpleDateFormat.parse("2020-06-14-00.00.00"),
                this.simpleDateFormat.parse("2020-12-31-23.59.59"),
                35455,
                0,
                34.50,
                Currency.getInstance("EUR")
        );
    }

    @Test()
    public void givenValidRequestWithNoPriceAssociated_thenRuntimeExceptionIsThrown() throws ParseException {
        GetCurrentPriceRequestDto givenRequest = new GetCurrentPriceRequestDto(
                "2028-06-14-10.00.00",
                35455,
                1
        );

        Date givenApplicationDate = this.simpleDateFormat.parse(givenRequest.applicationDate());

        when(
                this.priceRepositoryMock.findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                        givenRequest.productId(),
                        givenRequest.brandId(),
                        givenApplicationDate,
                        givenApplicationDate
                )
        ).thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> this.priceService.getCurrentPrice(givenRequest)
        );

        verify(
                this.priceRepositoryMock,
                times(1)
        ).findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                givenRequest.productId(),
                givenRequest.brandId(),
                givenApplicationDate,
                givenApplicationDate
        );


    }

}
