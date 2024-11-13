package creator;

import com.myappproducer.dto.TransactionDto;
import com.myappproducer.producer.TransactionEventProducer;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;

public class TransactionDtoCreator {

    public static TransactionDto entity() {
        return TransactionDto.builder()
                .transactionId(RandomStringUtils.randomAlphabetic(10))
                .senderBankCode(RandomStringUtils.randomNumeric(16))
                .receiverBankCode(RandomStringUtils.randomAlphabetic(16))
                .amount(new BigDecimal("12.5"))
                .build();
    }


}