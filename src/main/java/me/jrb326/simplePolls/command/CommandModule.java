package me.jrb326.simplePolls.command;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import javax.inject.Inject;
import me.jrb326.simplePolls.logging.InjectLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.bukkit.CloudBukkitCapabilities;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.LegacyPaperCommandManager;
import org.slf4j.Logger;

public class CommandModule extends AbstractModule {

    @InjectLogger
    private Logger logger;

    private final JavaPlugin plugin;

    @Inject
    public CommandModule(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Provides
    @Singleton
    public CommandManager<org.bukkit.command.CommandSender> provideCommandManager() {
        try {
            LegacyPaperCommandManager<org.bukkit.command.CommandSender> manager = new LegacyPaperCommandManager<>(
                    plugin,
                    ExecutionCoordinator.simpleCoordinator(),
                    java.util.function.Function.identity(),
                    java.util.function.Function.identity());

            if (manager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
                manager.registerBrigadier();
            }

            if (manager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
                manager.registerAsynchronousCompletions();
            }

            return manager;
        } catch (Exception e) {
            logger.error("Failed to create command manager", e);
            throw new RuntimeException(e);
        }
    }

    @Provides
    @Singleton
    public AnnotationParser<org.bukkit.command.CommandSender> provideAnnotationParser(
            CommandManager<org.bukkit.command.CommandSender> commandManager) {
        return new AnnotationParser<>(commandManager, org.bukkit.command.CommandSender.class);
    }

    @Override
    protected void configure() {
        // Commands will be bound individually in the main module
    }
}
