package casia.isiteam.springbootshiro.properties.other;

//import org.neo4j.driver.v1.AuthTokens;
//import org.neo4j.driver.v1.Config;
//import org.neo4j.driver.v1.Driver;
//import org.neo4j.driver.v1.GraphDatabase;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;

import java.util.concurrent.TimeUnit;

//@Configuration
//@ConditionalOnClass(Driver.class)
public class Neo4jAutoConfiguration {

//    @Autowired
//    private Environment environment;
//
//    @Bean
//    @ConditionalOnMissingBean
//    @ConditionalOnProperty("neo4j.uri")
//    public Driver driver() {
//        String timeout = environment.getProperty("neo4j.connect-timeout");
//        Config config;
//        if (timeout != null) {
//            config = Config.build().withConnectionTimeout(Integer.parseInt(timeout), TimeUnit.SECONDS).toConfig();
//        } else {
//            config = Config.defaultConfig();
//        }
//
//        String uri = environment.getProperty("neo4j.uri");
//        String username = environment.getProperty("neo4j.username");
//        String password = environment.getProperty("neo4j.password");
//        return GraphDatabase.driver(uri, AuthTokens.basic(username, password), config);
//    }
}
