package com.myappproducer.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.myappproducer.constant.ResponseConstants;
import com.myappproducer.dto.TransactionDto;
import com.myappproducer.dto.response.ResponseDto;
import com.myappproducer.producer.TransactionEventProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/")
public class TransactionEventController {

    private final TransactionEventProducer transactionEventProducer;


    public TransactionEventController(TransactionEventProducer transactionEventProducer) {
        this.transactionEventProducer = transactionEventProducer;
    }

    @PostMapping
    public ResponseEntity<ResponseDto<TransactionDto>> produceTransaction(@RequestBody TransactionDto transactionDto) throws JsonProcessingException {
        transactionEventProducer.produceTransactionEvent(transactionDto);
        
        var response = new ResponseDto<TransactionDto>();
        response.setCode(200);
        response.setMessage(ResponseConstants.SUCCESS.toString());
        response.setData(transactionDto);

        return ResponseEntity.ok(response);
    }




}
