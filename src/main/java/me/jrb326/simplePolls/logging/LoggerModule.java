// src/main/java/net/prismarine/prison/core/logging/LoggerModule.java
package me.jrb326.simplePolls.logging;

import com.google.inject.*;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import java.lang.reflect.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerModule extends AbstractModule {
    @Override
    protected void configure() {
        bindListener(Matchers.any(), new LoggerTypeListener());
    }

    @Provides
    @Singleton
    public Logger provideLogger() {
        // Default logger for general use; for class-specific, use @InjectLogger
        return LoggerFactory.getLogger("me.jrb326.simplePolls");
    }

    // TypeListener for injecting loggers into fields annotated with @InjectLogger
    private static class LoggerTypeListener implements TypeListener {
        @Override
        public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
            for (Field field : type.getRawType().getDeclaredFields()) {
                if (field.isAnnotationPresent(InjectLogger.class) && field.getType() == Logger.class) {
                    encounter.register((MembersInjector<I>) instance -> {
                        boolean accessible = field.isAccessible();
                        field.setAccessible(true);
                        try {
                            field.set(instance, LoggerFactory.getLogger(type.getRawType()));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                        field.setAccessible(accessible);
                    });
                }
            }
        }
    }
}
