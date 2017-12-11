package com.myzhihu.async;

import java.util.HashMap;
import java.util.Map;

public class EventModel {
    //发生的事件名称（比如点赞）
    private EventType type;
    //事件产生者（谁点赞）
    private int actorId;
    //事件发生的对象（对什么点赞）
    private int entityId;
    private int entityType;
    private int entityOwnerId;

    public EventModel(EventType type) {
        this.type = type;
    }

    private Map<String,String> exts = new HashMap<String,String>();
    public EventModel setExt(String key,String value){
        exts.put(key,value);
        return this;
    }
    public String getExt(String key){
        return exts.get(key);
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }
}
