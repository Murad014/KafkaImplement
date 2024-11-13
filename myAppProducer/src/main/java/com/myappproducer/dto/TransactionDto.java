package com.myappproducer.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class TransactionDto {
    private Long id;
    private String transactionId;
    private String senderBankCode;
    private String receiverBankCode;
    private BigDecimal amount;
}
