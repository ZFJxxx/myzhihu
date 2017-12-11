package com.myzhihu.async;

import java.util.List;

public interface EventHandler {
    //处理
    void doHandle(EventModel model);
    //找到自己关心的Event
    List<EventType> getSupportEventTypes();
}
