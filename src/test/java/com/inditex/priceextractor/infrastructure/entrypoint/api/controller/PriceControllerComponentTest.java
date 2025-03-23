package com.inditex.priceextractor.infrastructure.entrypoint.api.controller;

import com.inditex.priceextractor.PriceExtractorApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PriceExtractorApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PriceControllerComponentTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    MockMvc mvc;

    @Autowired
    SimpleDateFormat simpleDateFormat;

    @ParameterizedTest
    @MethodSource("provideGivenDataSet")
    public void givenValidRequest_whenGetCurrentPrice_thenStatus200AndValidResponse(
            String givenApplicationDateAsStr,
            long givenProductId,
            long givenBrandId,
            long expectedPriceList,
            String expectedStartDateAsStr,
            String expectedEndDateAsStr,
            double expectedPrice
    )
            throws Exception {

        int expectedProductId = 35455;
        int expectedBrandId = 1;

        mvc.perform(
                        get(
                                String.format(
                                        "/price?application_date=%s&product_id=%d&brand_id=%d",
                                        givenApplicationDateAsStr,
                                        givenProductId,
                                        givenBrandId
                                )
                        )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.price_list").value(expectedPriceList))
                .andExpect(MockMvcResultMatchers.jsonPath("$.product_id").value(expectedProductId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand_id").value(expectedBrandId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.start_date").value(expectedStartDateAsStr))
                .andExpect(MockMvcResultMatchers.jsonPath("$.end_date").value(expectedEndDateAsStr))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(expectedPrice))
                .andExpect(status().isOk());
    }

    private static Stream<Arguments> provideGivenDataSet() {
        return Stream.of(
                Arguments.of("2020-06-14-10.00.00", 35455, 1, 1, "2020-06-14-00.00.00", "2020-12-31-23.59.59", 35.50),
                Arguments.of("2020-06-14-16.00.00", 35455, 1, 2, "2020-06-14-15.00.00", "2020-06-14-18.30.00", 25.45),
                Arguments.of("2020-06-14-21.00.00", 35455, 1, 1, "2020-06-14-00.00.00", "2020-12-31-23.59.59", 35.50),
                Arguments.of("2020-06-15-10.00.00", 35455, 1, 3, "2020-06-15-00.00.00", "2020-06-15-11.00.00", 30.50),
                Arguments.of("2020-06-16-21.00.00", 35455, 1, 4, "2020-06-15-16.00.00", "2020-12-31-23.59.59", 38.95)
        );
    }

    @Test
    public void givenRequestWithNotPriceAssociated_whenGetCurrentPrice_thenStatus204()
            throws Exception {

        String givenApplicationDateAsStr = "2020-06-14-10.00.00";
        long givenProductId = 2556;
        long givenBrandId = 1;

        mvc.perform(
                        get(
                                String.format(
                                        "/price?application_date=%s&product_id=%d&brand_id=%d",
                                        givenApplicationDateAsStr,
                                        givenProductId,
                                        givenBrandId
                                )
                        )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist())
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenRequestWithInvalidDateFormat_whenGetCurrentPrice_thenStatus403()
            throws Exception {

        String givenApplicationDateAsStr = "cmt:2020-06-14-10.00.00";
        long givenProductId = 2556;
        long givenBrandId = 1;

        mvc.perform(
                        get(
                                String.format(
                                        "/price?application_date=%s&product_id=%d&brand_id=%d",
                                        givenApplicationDateAsStr,
                                        givenProductId,
                                        givenBrandId
                                )
                        )
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNotValidQueryParams_whenGetCurrentPrice_thenStatus400()
            throws Exception {

        mvc.perform(get("/price")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
