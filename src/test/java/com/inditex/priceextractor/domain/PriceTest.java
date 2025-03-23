package com.inditex.priceextractor.domain;

import com.inditex.priceextractor.infrastructure.format.date.SimpleDateFormatConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PriceTest {

    SimpleDateFormat simpleDateFormat;

    @BeforeEach
    public void setup() {
        this.simpleDateFormat = new SimpleDateFormatConfig().simpleDateFormat();
    }

    @Test
    public void givenValidData_thenShouldWork() throws ParseException {
        long givenPriceId = 1;
        long givenBrandId= 2;
        Date givenStartDate = this.simpleDateFormat.parse("2020-06-14-00.00.00");
        Date givenEndDate = this.simpleDateFormat.parse("2020-12-31-23.59.59");
        long givenProductId = 35455;
        int givenPriority = 0;
        double givenPrice = 35.40;
        Currency givenCurrency = Currency.getInstance("EUR");

        Price price = new Price(
                givenPriceId,
                givenBrandId,
                givenStartDate,
                givenEndDate,
                givenProductId,
                givenPriority,
                givenPrice,
                givenCurrency
        );

        Assertions.assertEquals(price.getId(), givenPriceId);
        Assertions.assertEquals(price.getBrandId(), givenBrandId);
        Assertions.assertEquals(price.getStartDate(), givenStartDate);
        Assertions.assertEquals(price.getEndDate(), givenEndDate);
        Assertions.assertEquals(price.getProductId(), givenProductId);
        Assertions.assertEquals(price.getPriority(), givenPriority);
        Assertions.assertEquals(price.getPrice(), givenPrice);
        Assertions.assertEquals(price.getCurr(), givenCurrency);

    }

    @Test
    public void givenEndDateNewerThanStartDate_thenThrowRunTimeException() {

        assertThrows(
                RuntimeException.class,
                () -> new Price(
                        1,
                        2,
                        this.simpleDateFormat.parse("2020-06-14-00.00.00"),
                        this.simpleDateFormat.parse("2020-06-13-00.00.00"),
                        35455,
                        0,
                        35.40,
                        Currency.getInstance("EUR")
                )
        );
    }
}
