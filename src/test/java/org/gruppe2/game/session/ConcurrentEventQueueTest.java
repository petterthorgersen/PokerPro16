package org.gruppe2.game.session;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.gruppe2.game.event.Event;
import org.junit.Before;
import org.junit.Test;

public class ConcurrentEventQueueTest {
    private ConcurrentEventQueue queue;

    private static class MockEvent implements Event {}
    private static class MockEvent2 implements Event {}

    @Before
    public void setup() {
        queue = new ConcurrentEventQueue();
    }

    /**
     * Processing with an empty queue shouldn't block or throw an exception.
     */
    @Test
    public void emptyQueueTest() {
        queue.process();
    }

    /**
     * Processing when there is an event, but no handler for the event,
     * shouldn't throw exceptions or block.
     */
    @Test
    public void processWithOneEventWithNoHandlerTest() {
        queue.addEvent(new MockEvent());
        queue.process();
    }

    /**
     * When processing, the entire queue should be consumed.
     */
    @Test
    public void processConsumesEventTest() {
        queue.addEvent(new MockEvent());

        assertEquals(1, queue.size());

        queue.process();

        assertEquals(0, queue.size());
    }

    /**
     * If there are no events waiting, but there is a handler for the event,
     * then the handler shouldn't do anything.
     */
    @Test
    public void eventHandlerWithNoEventTest() {
        final boolean[] handled = {false};

        queue.registerHandler(MockEvent.class, event -> handled[0] = true);
        queue.process();

        assertFalse(handled[0]);
    }

    /**
     * If there is an event waiting and a handler for the event, the event should be handled.
     */
    @Test
    public void eventHandlerWithEventTest() {
        final boolean[] handled = {false};

        queue.addEvent(new MockEvent());
        queue.registerHandler(MockEvent.class, event -> handled[0] = true);
        queue.process();

        assertTrue(handled[0]);
    }

    /**
     * If there are no events waiting, but there is a generic handler,
     * then the handler shouldn't do anything.
     */
    @Test
    public void genericEventHandlerWithNoEventTest() {
        final boolean[] handled = {false};

        queue.registerHandler(Event.class, event -> handled[0] = true);
        queue.process();

        assertFalse(handled[0]);
    }

    /**
     * If there is an event waiting and there is a generic handler, then the
     * handler should handle it.
     */
    @Test
    public void genericEventHandlerWithEventTest() {
        final boolean[] handled = {false};

        queue.addEvent(new MockEvent());
        queue.registerHandler(Event.class, event -> handled[0] = true);
        queue.process();

        assertTrue(handled[0]);
    }

    /**
     * If there are multiple event handlers, but no events,
     * then nothing happens.
     */
    @Test
    public void multipleEventHandlersWithNoEventTest() {
        final int[] handled = {0, 0};

        queue.registerHandler(MockEvent.class, event -> handled[0]++);
        queue.registerHandler(MockEvent2.class, event -> handled[1]++);
        queue.registerHandler(MockEvent.class, event -> handled[0]++);
        queue.registerHandler(MockEvent2.class, event -> handled[1]++);
        queue.registerHandler(MockEvent.class, event -> handled[0]++);
        queue.process();

        assertEquals(0, handled[0]);
        assertEquals(0, handled[1]);
    }

    /**
     * If there are multiple events and multiple event handlers,
     * then they should be handled.
     */
    @Test
    public void multipleEventHandlersWithEventsTest() {
        final int[] handled = {0, 0};

        queue.addEvent(new MockEvent());
        queue.addEvent(new MockEvent2());
        queue.addEvent(new MockEvent());

        queue.registerHandler(MockEvent.class, event -> handled[0]++);
        queue.registerHandler(MockEvent2.class, event -> handled[1]++);
        queue.registerHandler(MockEvent.class, event -> handled[0]++);
        queue.registerHandler(MockEvent2.class, event -> handled[1]++);
        queue.registerHandler(MockEvent.class, event -> handled[0]++);
        queue.process();

        assertEquals(6, handled[0]);
        assertEquals(2, handled[1]);
    }

    /**
     * If there are multiple event handlers, but no events,
     * then nothing happens.
     */
    @Test
    public void multipleEventHandlersAndGenericHandlerWithNoEventTest() {
        final int[] handled = {0, 0, 0};

        queue.registerHandler(MockEvent.class, event -> handled[0]++);
        queue.registerHandler(MockEvent2.class, event -> handled[1]++);
        queue.registerHandler(MockEvent.class, event -> handled[0]++);
        queue.registerHandler(MockEvent2.class, event -> handled[1]++);
        queue.registerHandler(MockEvent.class, event -> handled[0]++);
        queue.registerHandler(Event.class, event -> handled[2]++);
        queue.process();

        assertEquals(0, handled[0]);
        assertEquals(0, handled[1]);
        assertEquals(0, handled[2]);
    }

    /**
     * If there are multiple events and multiple event handlers,
     * then they should be handled.
     */
    @Test
    public void multipleEventHandlersAndGenericHandlerWithEventsTest() {
        final int[] handled = {0, 0, 0};

        queue.addEvent(new MockEvent());
        queue.addEvent(new MockEvent2());
        queue.addEvent(new MockEvent());

        queue.registerHandler(MockEvent.class, event -> handled[0]++);
        queue.registerHandler(MockEvent2.class, event -> handled[1]++);
        queue.registerHandler(MockEvent.class, event -> handled[0]++);
        queue.registerHandler(MockEvent2.class, event -> handled[1]++);
        queue.registerHandler(MockEvent.class, event -> handled[0]++);
        queue.registerHandler(Event.class, event -> handled[2]++);
        queue.process();

        assertEquals(6, handled[0]);
        assertEquals(2, handled[1]);
        assertEquals(3, handled[2]);
    }
}