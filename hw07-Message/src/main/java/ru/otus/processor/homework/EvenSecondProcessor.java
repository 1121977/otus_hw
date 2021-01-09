package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.DateTimeProvider;
import ru.otus.processor.Processor;
import java.time.DateTimeException;
import java.time.LocalTime;

public class EvenSecondProcessor implements Processor {
//    private LocalTime time;
    private DateTimeProvider dateTimeProvider;

    public EvenSecondProcessor(DateTimeProvider dateTimeProvider){
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if (dateTimeProvider.getDateTime().getSecond() % 2 == 0) {
            throw new DateTimeException("Even second");
        }
        return message;
    }

    public void setDateTimeProvider(DateTimeProvider dateTimeProvider){
        this.dateTimeProvider = dateTimeProvider;
    }
}
