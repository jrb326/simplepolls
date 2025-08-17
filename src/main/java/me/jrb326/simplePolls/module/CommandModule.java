package me.jrb326.simplePolls.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import me.jrb326.simplePolls.SimplePolls;
import me.jrb326.simplePolls.commands.CreatePollCommand;
import me.jrb326.simplePolls.gui.ChatInputHandler;
import me.jrb326.simplePolls.gui.CreatePollGui;
import me.jrb326.simplePolls.service.PollService;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;

import javax.inject.Singleton;

public class CommandModule extends AbstractModule {

    private final SimplePolls plugin;

    public CommandModule(SimplePolls plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(JavaPlugin.class).toInstance(plugin);
        bind(SimplePolls.class).toInstance(plugin);
        
        // Bind services and commands
        bind(PollService.class).in(Singleton.class);
        bind(ChatInputHandler.class).in(Singleton.class);
        bind(CreatePollGui.class).in(Singleton.class);
        bind(CreatePollCommand.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public CommandManager<org.bukkit.command.CommandSender> provideCommandManager() {
        try {
            return PaperCommandManager.createNative(plugin, ExecutionCoordinator.simpleCoordinator());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create command manager", e);
        }
    }

    @Provides
    @Singleton
    public AnnotationParser<org.bukkit.command.CommandSender> provideAnnotationParser(
            CommandManager<org.bukkit.command.CommandSender> commandManager) {
        return new AnnotationParser<>(commandManager, org.bukkit.command.CommandSender.class);
    }
}