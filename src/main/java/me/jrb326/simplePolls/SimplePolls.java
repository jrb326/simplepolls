package me.jrb326.simplePolls;

import com.google.inject.Guice;
import com.google.inject.Injector;
import me.jrb326.simplePolls.command.CommandModule;
import me.jrb326.simplePolls.command.PollCommand;
import me.jrb326.simplePolls.database.DatabaseModule;
import me.jrb326.simplePolls.gui.GuiManager;
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
        try {
            // Create injector with all modules
            injector =
                    Guice.createInjector(new LoggerModule(), new DatabaseModule(), new CommandModule(this), binder -> {
                        binder.bind(JavaPlugin.class).toInstance(this);
                        binder.bind(GuiManager.class).asEagerSingleton();
                        binder.bind(PollCommand.class).asEagerSingleton();
                    });

            // Inject dependencies into this instance
            injector.injectMembers(this);

            logger.info("Initializing SimplePolls plugin...");

            // Register commands
            registerCommands();

            logger.info("SimplePolls plugin enabled successfully!");

        } catch (Exception e) {
            logger.error("Failed to initialize SimplePolls plugin", e);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void registerCommands() {
        try {
            AnnotationParser<org.bukkit.command.CommandSender> parser = injector.getInstance(AnnotationParser.class);
            PollCommand pollCommand = injector.getInstance(PollCommand.class);

            parser.parse(pollCommand);
            logger.info("Registered /poll command");

        } catch (Exception e) {
            logger.error("Failed to register commands", e);
            throw e;
        }
    }

    @Override
    public void onDisable() {
        logger.info("SimplePolls plugin disabled.");
    }
}
