package ru.otus.processor.homework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.DateTimeException;
import java.time.LocalTime;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

class EvenSecondProcessorTest {

    Message message;
    EvenSecondProcessor evenSecondProcessor;

    @BeforeEach
    void setUp() {
        message = Mockito.mock(Message.class);
        evenSecondProcessor = new EvenSecondProcessor();
    }

    @Test
    void processTest() {
        LocalTime time = LocalTime.now();
        if (time.getSecond() % 2 != 0) {
            time.plusSeconds(1L);
        }
        evenSecondProcessor.setTime(time);
        assertThrows(DateTimeException.class, () -> evenSecondProcessor.process(message));
        evenSecondProcessor.setTime(time.minusSeconds(1L));
        assertEquals(message, evenSecondProcessor.process(message));
    }
}