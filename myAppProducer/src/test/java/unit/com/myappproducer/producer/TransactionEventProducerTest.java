package com.myappproducer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myappproducer.creator.TransactionDtoCreator;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionEventProducerTest {

    @Mock
    KafkaTemplate<Long, String> kafkaTemplate;

    @Spy
    ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    TransactionEventProducer transactionEventProducer;

    private final static String TOPIC_NAME = "transaction-events";

    @Test
    @DisplayName("Transaction Event Producer")
    void givenTransactionDto_whenSendSuccessfully_thenReturnCompatibleFutureWithSendResult() throws JsonProcessingException, ExecutionException, InterruptedException {
        // Arrange
        var transactionDto = TransactionDtoCreator.entity();
        transactionDto.setId(1L);
        var message = objectMapper.writeValueAsString(transactionDto);

        // Mocking SendResult and Kafka interaction
        ProducerRecord<Long, String> producerRecord = new ProducerRecord<>("transaction-events", 1L, message);
        RecordMetadata recordMetadata = mock(RecordMetadata.class);
        SendResult<Long, String> sendResult = new SendResult<>(producerRecord, recordMetadata);
        CompletableFuture<SendResult<Long, String>> future = CompletableFuture.completedFuture(sendResult);

        // When
        when(kafkaTemplate.send(anyString(), anyLong(), anyString())).thenReturn(future);

        // Act
        CompletableFuture<SendResult<Long, String>> resultKafka = transactionEventProducer.produceTransactionEvent(transactionDto);

        // Verify
        verify(objectMapper, times(2)).writeValueAsString(transactionDto);
        verify(kafkaTemplate, times(1)).send(TOPIC_NAME,
                transactionDto.getId(), message);

        // Assert
        assertTrue(resultKafka.isDone());
        assertFalse(resultKafka.isCompletedExceptionally());
        assertNotNull(resultKafka);
        assertEquals(sendResult.getProducerRecord().value(), resultKafka.get().getProducerRecord().value());
    }

    @Test
    @DisplayName("Transaction Event Producer Failure")
    void givenTransactionDto_whenSendUnsuccessfully_thenReturnException() throws JsonProcessingException {
        // Arrange
        var transactionDto = TransactionDtoCreator.entity();
        var message = objectMapper.writeValueAsString(transactionDto);

        // Creating a future that completes exceptionally.
        CompletableFuture<SendResult<Long, String>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Send failed"));

        // Stubbing KafkaTemplate to return a failed CompletableFuture on send.
        when(kafkaTemplate.send(TOPIC_NAME, transactionDto.getId(), message)).thenReturn(future);

        // Act
        var resultFuture = transactionEventProducer.produceTransactionEvent(transactionDto);

        // Then
        assertNotNull(resultFuture);
        assertTrue(resultFuture.isCompletedExceptionally());

        ExecutionException exception = assertThrows(ExecutionException.class, resultFuture::get);
        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("Send failed", exception.getCause().getMessage());
    }


}