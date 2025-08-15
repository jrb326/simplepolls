package me.jrb326.simplePolls.commands;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import me.jrb326.simplePolls.SimplePolls;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.bukkit.CloudBukkitCapabilities;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.LegacyPaperCommandManager;

public class CommandModule extends AbstractModule {

    private final SimplePolls plugin;

    public CommandModule(SimplePolls plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        // Commands will be registered via the manager
    }

    @Provides
    @Singleton
    public CommandManager<org.bukkit.command.CommandSender> provideCommandManager() {
        LegacyPaperCommandManager<org.bukkit.command.CommandSender> manager =
                LegacyPaperCommandManager.createNative(plugin, ExecutionCoordinator.simpleCoordinator());

        if (manager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            manager.registerBrigadier();
        }

        if (manager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            manager.registerAsynchronousCompletions();
        }

        return manager;
    }

    @Provides
    @Singleton
    public AnnotationParser<org.bukkit.command.CommandSender> provideAnnotationParser(
            CommandManager<org.bukkit.command.CommandSender> commandManager) {
        return new AnnotationParser<>(commandManager, org.bukkit.command.CommandSender.class);
    }
}
