package pt.fabiopina.queue;

import pt.fabiopina.entities.EventInfoEntity;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class EventQueue {
    BlockingQueue<EventInfoEntity> events;

    public EventQueue() {
        events = new LinkedBlockingDeque<>();
    }

    public EventInfoEntity getEvent() throws Exception {
        return events.take();
    }

    public void addEvent(EventInfoEntity event) {
        events.add(event);
    }
}
