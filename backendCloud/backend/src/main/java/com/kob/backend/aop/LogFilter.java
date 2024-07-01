package com.kob.backend.aop;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class LogFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent iLoggingEvent) {
        String path = ControllerLogAspect.class.getName();
        if(iLoggingEvent.getLoggerName().equals(path))
            return FilterReply.ACCEPT;
        else
            return FilterReply.DENY;
    }
}
