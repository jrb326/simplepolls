package me.jrb326.simplePolls;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;

public final class SimplePolls extends JavaPlugin {

    private Injector injector;

    @Override
    public void onEnable() {
        // Plugin startup logic
        injector = Guice.createInjector();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
