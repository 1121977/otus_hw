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
    Processor evenSecondProcessor;

    @BeforeEach
    void setUp() {
        message = Mockito.mock(Message.class);
        evenSecondProcessor = new EvenSecondProcessor();
    }

    @Test
    void processTest() {
        try {
            if(LocalTime.now().getSecond()%2==0){
                sleep(1000L);
            }
            sleep(1000L-System.currentTimeMillis()%1000);
        }
        catch (InterruptedException e){
            System.out.println("Test interrupted");
            assertFalse(true, "Test interrupted");
        }
        assertThrows(DateTimeException.class, () -> evenSecondProcessor.process(message));
    }
}