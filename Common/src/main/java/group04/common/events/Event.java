/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group04.common.events;

/**
 *
 * @author burno
 */
public class Event {
    private final EventType type;
    private final String entityID;

    public Event(EventType type, String entityID) {
        this.type = type;
        this.entityID = entityID;
    }

    public EventType getType() {
        return type;
    }

    public String getEntityID() {
        return entityID;
    }
}
