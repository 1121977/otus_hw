package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;
import java.time.DateTimeException;
import java.time.LocalTime;

public class EvenSecondProcessor implements Processor {
    private LocalTime time;

    @Override
    public Message process(Message message) {
        if (time==null) { time = LocalTime.now(); }
        if (time.getSecond() % 2 == 0) {
            throw new DateTimeException("Even second");
        }
        return message;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
