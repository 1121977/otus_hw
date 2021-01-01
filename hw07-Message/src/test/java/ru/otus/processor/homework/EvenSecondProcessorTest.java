package ru.otus.processor.homework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.model.Message;
import ru.otus.processor.DateTimeProvider;
import ru.otus.processor.Processor;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class EvenSecondProcessorTest {

    Message message;
    EvenSecondProcessor evenSecondProcessor;

    @BeforeEach
    void setUp() {
        message = Mockito.mock(Message.class);
    }

    @Test
    void processTest() {
        DateTimeProvider evenSecondProvider = () -> {
            LocalDateTime localDateTime = LocalDateTime.now();
            return localDateTime.getSecond()%2 !=0 ? localDateTime.plusSeconds(1L):localDateTime;
        };
        evenSecondProcessor = new EvenSecondProcessor(evenSecondProvider);
        assertThrows(DateTimeException.class, () -> evenSecondProcessor.process(message));
        DateTimeProvider oddSecondProvider = () -> evenSecondProvider.getDateTime().minusSeconds(1L);
        evenSecondProcessor.setDateTimeProvider(oddSecondProvider);
        assertEquals(message, evenSecondProcessor.process(message));
    }
}