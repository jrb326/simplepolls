package me.jrb326.simplePolls;

import com.google.inject.Guice;
import com.google.inject.Injector;
import me.jrb326.simplePolls.commands.CreatePollCommand;
import me.jrb326.simplePolls.gui.ChatInputHandler;
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
        // Plugin startup logic
        logger.info("Enabling SimplePolls plugin...");
        
        // Create Guice injector with modules
        injector = Guice.createInjector(
            new LoggerModule(),
            new CommandModule(this)
        );
        
        // Inject dependencies into this instance
        injector.injectMembers(this);
        
        // Get the annotation parser and register commands
        annotationParser = injector.getInstance(AnnotationParser.class);
        
        // Register commands
        CreatePollCommand createPollCommand = injector.getInstance(CreatePollCommand.class);
        annotationParser.parse(createPollCommand);
        
        // Register event listeners
        ChatInputHandler chatInputHandler = injector.getInstance(ChatInputHandler.class);
        getServer().getPluginManager().registerEvents(chatInputHandler, this);
        
        logger.info("SimplePolls plugin enabled successfully!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (logger != null) {
            logger.info("Disabling SimplePolls plugin...");
        }
    }
}
