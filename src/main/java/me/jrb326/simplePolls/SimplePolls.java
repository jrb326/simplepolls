package me.jrb326.simplePolls;

import com.google.inject.Guice;
import com.google.inject.Injector;
import me.jrb326.simplePolls.commands.CommandModule;
import me.jrb326.simplePolls.commands.PollCommands;
import me.jrb326.simplePolls.database.DatabaseModule;
import me.jrb326.simplePolls.logging.InjectLogger;
import me.jrb326.simplePolls.logging.LoggerModule;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.annotations.AnnotationParser;
import org.slf4j.Logger;

public final class SimplePolls extends JavaPlugin {

    @InjectLogger
    private Logger logger;

    private Injector injector;

    @Override
    public void onEnable() {
        // Initialize Guice injector with modules
        injector = Guice.createInjector(new LoggerModule(), new DatabaseModule(this), new CommandModule(this));

        // Inject logger into this instance
        injector.injectMembers(this);

        logger.info("Starting SimplePolls plugin...");

        // Register commands
        registerCommands();

        logger.info("SimplePolls plugin enabled successfully!");
    }

    @Override
    public void onDisable() {
        if (logger != null) {
            logger.info("SimplePolls plugin disabled.");
        }
    }

    private void registerCommands() {
        try {
            AnnotationParser<org.bukkit.command.CommandSender> annotationParser =
                    injector.getInstance(AnnotationParser.class);
            PollCommands pollCommands = injector.getInstance(PollCommands.class);

            annotationParser.parse(pollCommands);
            logger.info("Commands registered successfully.");
        } catch (Exception e) {
            logger.error("Failed to register commands", e);
        }
    }
}
