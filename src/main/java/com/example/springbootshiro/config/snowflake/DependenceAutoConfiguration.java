package com.example.springbootshiro.config.snowflake;


import com.example.springbootshiro.utils.ApplicationContextUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author wangzw
 */
@Configuration
@ConfigurationProperties(prefix = "project")
@ConfigurationPropertiesScan("${project.base-packages}")
@Import({IDGeneratorConfig.class})
public class DependenceAutoConfiguration {

    private String basePackages;

    @Bean
    public IDGenerator idGenerator(IDGeneratorConfig config) {

        String strategy = config.getStrategy();

        if (IDGeneratorConfig.STRATEGY_DEFAULT.equals(strategy)) {
            return IDGenerator.snowflake53bits(config.getWorkerId());
        } else if (IDGeneratorConfig.STRATEGY_STANDARD.equals(strategy)) {
            return IDGenerator.snowflake(config.getWorkerId(), config.getBizId());
        }

        return null;

    }

//    @Bean
//    public ApplicationContextUtil applicationContextUtils(ApplicationContext context){
//        ApplicationContextUtil contextUtils =  new ApplicationContextUtil();
//        contextUtils.setApplicationContext(context);
//        return contextUtils;
//    }


    public String getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String basePackages) {
        this.basePackages = basePackages;
    }
}
