package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.DateTimeException;
import java.time.LocalTime;

public class EvenSecondProcessor implements Processor {
    @Override
    public Message process(Message message) {
        if (LocalTime.now().getSecond()%2==0){
            throw new DateTimeException("Even second");
        }
        return message;
    }
}
