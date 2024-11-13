package com.myappproducer.creator;

import com.myappproducer.dto.TransactionDto;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;

public class TransactionDtoCreator {

    public static TransactionDto entity() {
        var transactionDto = new TransactionDto();
        transactionDto.setTransactionId(RandomStringUtils.randomAlphabetic(10));
        transactionDto.setSenderBankCode(RandomStringUtils.randomNumeric(16));
        transactionDto.setReceiverBankCode(RandomStringUtils.randomAlphabetic(16));
        transactionDto.setAmount(new BigDecimal("12.5"));

        return transactionDto;
    }


}