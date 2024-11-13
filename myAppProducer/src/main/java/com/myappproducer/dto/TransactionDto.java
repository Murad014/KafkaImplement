package com.myappproducer.dto;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TransactionDto {
    private Long id;
    private String transactionId;
    private String senderBankCode;
    private String receiverBankCode;
    private BigDecimal amount;
}
