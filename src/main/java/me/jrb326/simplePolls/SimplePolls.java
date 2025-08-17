package me.jrb326.simplePolls;

import com.google.inject.Guice;
import com.google.inject.Injector;
import me.jrb326.simplePolls.commands.PollCommand;
import me.jrb326.simplePolls.commands.CreatePollCommand;
import me.jrb326.simplePolls.commands.PollCommands;
import me.jrb326.simplePolls.database.DatabaseModule;
import me.jrb326.simplePolls.gui.ChatInputHandler;
import me.jrb326.simplePolls.gui.GuiManager;
import me.jrb326.simplePolls.logging.InjectLogger;
import me.jrb326.simplePolls.logging.LoggerModule;
import me.jrb326.simplePolls.module.CommandModule;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.annotations.AnnotationParser;
import org.slf4j.Logger;

public final class SimplePolls extends JavaPlugin {

    @InjectLogger
    private Logger logger;

    private Injector injector;
    private AnnotationParser<org.bukkit.command.CommandSender> annotationParser;

    @Override
    public void onEnable() {
        try {
            // Plugin startup logic
            logger.info("Enabling SimplePolls plugin...");
            
            // Create Guice injector with modules
            injector = Guice.createInjector(
                new LoggerModule(),
                new DatabaseModule(this),
                new CommandModule(this)
            );
            
            // Inject dependencies into this instance
            injector.injectMembers(this);
            
            // Get the annotation parser and register commands
            annotationParser = injector.getInstance(AnnotationParser.class);
            
            // Register commands
            registerCommands();
            
            // Register event listeners
            ChatInputHandler chatInputHandler = injector.getInstance(ChatInputHandler.class);
            getServer().getPluginManager().registerEvents(chatInputHandler, this);
            
            logger.info("SimplePolls plugin enabled successfully!");
            
        } catch (Exception e) {
            logger.error("Failed to initialize SimplePolls plugin", e);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (logger != null) {
            logger.info("Disabling SimplePolls plugin...");
        }
    }

    private void registerCommands() {
        try {
            // Register CreatePoll command
            CreatePollCommand createPollCommand = injector.getInstance(CreatePollCommand.class);
            annotationParser.parse(createPollCommand);
            
            // Register staff commands  
            PollCommands pollCommands = injector.getInstance(PollCommands.class);
            annotationParser.parse(pollCommands);
            
            // Register poll display command
            PollCommand pollCommand = injector.getInstance(PollCommand.class);
            annotationParser.parse(pollCommand);
            
            logger.info("Commands registered successfully.");
        } catch (Exception e) {
            logger.error("Failed to register commands", e);
            throw e;
        }
    }
}
