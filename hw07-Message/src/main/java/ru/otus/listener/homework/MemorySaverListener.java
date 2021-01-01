package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MemorySaverListener implements Listener {

    private final List<OldNewMessageBundle> messagesCredential = new ArrayList<>();

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        messagesCredential.add(new OldNewMessageBundle(oldMsg,newMsg));
    }

    class OldNewMessageBundle {
        Message oldMessage;
        Message newMessage;

        OldNewMessageBundle(Message oldMessage, Message newMessage) {
            this.oldMessage = oldMessage.clone();
            this.newMessage = newMessage.clone();
        }
    }
}
