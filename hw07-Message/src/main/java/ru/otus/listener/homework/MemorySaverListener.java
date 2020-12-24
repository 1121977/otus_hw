package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;
import java.util.ArrayList;
import java.util.List;

public class MemorySaverListener implements Listener {

    private List<OldNewMessageBundle> messagesCredential = new ArrayList<>();

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        messagesCredential.add(new OldNewMessageBundle(oldMsg,newMsg));
    }

    class OldNewMessageBundle {
        Message oldMessage;
        Message newMessage;

        OldNewMessageBundle(Message oldMessage, Message newMessage) {
            this.oldMessage = oldMessage.toBuilder().build();
            this.newMessage = newMessage.toBuilder().build();
        }
    }
}
