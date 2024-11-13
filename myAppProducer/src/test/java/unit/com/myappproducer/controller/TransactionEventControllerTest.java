package com.myappproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myappproducer.constant.ResponseConstants;
import com.myappproducer.creator.TransactionDtoCreator;
import com.myappproducer.dto.TransactionDto;
import com.myappproducer.dto.response.ResponseDto;
import com.myappproducer.producer.TransactionEventProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionEventController.class)
@AutoConfigureMockMvc
class TransactionEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private TransactionEventProducer transactionEventProducer;

    private static final String MAIN_URL = "/api/v1/";

    @Test
    @DisplayName("Transaction Produce")
    void produceTransaction() throws Exception {
        // given
        var transactionDto = TransactionDtoCreator.entity();
        var dtoToString = objectMapper.writeValueAsString(transactionDto);


        // act
        var response = mockMvc.perform(post(MAIN_URL)
                        .content(dtoToString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // verify
        Mockito.verify(transactionEventProducer, Mockito.times(1))
                .produceTransactionEvent(any());

        ResponseDto<TransactionDto> res = objectMapper.readValue(response.getResponse().getContentAsString(),
                new TypeReference<>() {});

        // assert
        assertEquals(ResponseConstants.SUCCESS.toString(), res.getMessage());
        assertEquals(200, res.getCode());
    }
}