package me.jrb326.simplePolls.config;

import me.jrb326.simplePolls.SimplePolls;
import org.bukkit.configuration.file.FileConfiguration;

public class DatabaseConfig {
    private final String url;
    private final String username;
    private final String password;
    private final int maxPoolSize;

    public DatabaseConfig(SimplePolls plugin) {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();

        // Set defaults if not present
        config.addDefault("database.url", "jdbc:mysql://localhost:3306/simplepolls");
        config.addDefault("database.username", "root");
        config.addDefault("database.password", "");
        config.addDefault("database.maxPoolSize", 10);
        config.options().copyDefaults(true);
        plugin.saveConfig();

        this.url = config.getString("database.url");
        this.username = config.getString("database.username");
        this.password = config.getString("database.password");
        this.maxPoolSize = config.getInt("database.maxPoolSize");
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }
}
