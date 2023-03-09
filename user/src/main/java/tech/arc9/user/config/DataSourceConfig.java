package tech.arc9.user.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tech.arc9.user.server.UserServiceImpl;

import javax.annotation.Resource;
import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "tech.arc9.user.db.mysql.repository")
public class DataSourceConfig {
    private static final String MYSQL_HOST = "MYSQL_HOST";
    private static final String MYSQL_PORT = "MYSQL_PORT";
    private static final String MYSQL_DATABASE = "MYSQL_DATABASE";
    private static final String MYSQL_USERNAME = "MYSQL_USERNAME";
    private static final String MYSQL_PASSWORD = "MYSQL_PASSWORD";

    @Resource
    private Environment env;
    @Bean
    public DataSource getDataSource() {

        String host = env.getRequiredProperty(MYSQL_HOST);
        int port = Integer.parseInt(env.getRequiredProperty(MYSQL_PORT));
        String database = env.getRequiredProperty(MYSQL_DATABASE);
        String username = env.getRequiredProperty(MYSQL_USERNAME);
        String password = env.getRequiredProperty(MYSQL_PASSWORD);

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:mysql://" + host + ":" + port + "/" + database + "?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true");
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        DataSource dataSource = dataSourceBuilder.build();
        return new TransactionAwareDataSourceProxy(dataSource);
    }
}
