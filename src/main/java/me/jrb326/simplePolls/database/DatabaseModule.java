package me.jrb326.simplePolls.database;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import me.jrb326.simplePolls.SimplePolls;
import me.jrb326.simplePolls.config.DatabaseConfig;
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
    public DataSource provideDataSource(DatabaseConfig databaseConfig) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(databaseConfig.getUrl());
        config.setUsername(databaseConfig.getUsername());
        config.setPassword(databaseConfig.getPassword());
        config.setMaximumPoolSize(databaseConfig.getMaxPoolSize());
        config.setConnectionTestQuery("SELECT 1");
        return new HikariDataSource(config);
    }

    @Provides
    @Singleton
    public Jdbi provideJdbi(DataSource dataSource) {
        return Jdbi.create(dataSource).installPlugin(new SqlObjectPlugin());
    }
}
