package com.kob.backend.aop;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;

import java.util.Arrays;
import java.util.Map;

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
