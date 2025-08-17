package me.jrb326.simplePolls.database;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import me.jrb326.simplePolls.SimplePolls;
import me.jrb326.simplePolls.config.DatabaseConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class DatabaseModule extends AbstractModule {

    private final SimplePolls plugin;

    public DatabaseModule(SimplePolls plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        // Bind DAO interfaces to implementations
        bind(PollDAO.class);
    }

    @Provides
    @Singleton
    public DatabaseConfig provideDatabaseConfig() {
        return new DatabaseConfig(plugin);
    }

    @Provides
    @Singleton
    public DataSource provideDataSource() {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getString("database.url", "jdbc:mysql://localhost:3306/simplepolls"));
        hikariConfig.setUsername(config.getString("database.username", "root"));
        hikariConfig.setPassword(config.getString("database.password", "password"));
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setMaximumPoolSize(config.getInt("database.pool.maximumPoolSize", 10));
        hikariConfig.setMinimumIdle(config.getInt("database.pool.minimumIdle", 2));
        hikariConfig.setConnectionTimeout(config.getLong("database.pool.connectionTimeout", 30000));
        hikariConfig.setIdleTimeout(config.getLong("database.pool.idleTimeout", 600000));
        hikariConfig.setMaxLifetime(config.getLong("database.pool.maxLifetime", 1800000));
        hikariConfig.setConnectionTestQuery("SELECT 1");

        return new HikariDataSource(hikariConfig);
    }

    @Provides
    @Singleton
    public Jdbi provideJdbi(DataSource dataSource) {
        Jdbi jdbi = Jdbi.create(dataSource);
        jdbi.installPlugin(new SqlObjectPlugin());

        // Configure timestamp handling for LocalDateTime
        jdbi.registerColumnMapper(java.time.LocalDateTime.class, (rs, columnNumber, ctx) -> {
            java.sql.Timestamp timestamp = rs.getTimestamp(columnNumber);
            return timestamp != null ? timestamp.toLocalDateTime() : null;
        });

        return jdbi;
    }
}
