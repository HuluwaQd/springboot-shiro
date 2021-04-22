package com.example.springbootshiro.config.opendoc;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.Api;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangzb
 * @version 1.0
 * @date 2021/02/19
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
@ConditionalOnProperty(prefix = "project.doc", name ="enable" , havingValue = "true")
public class SwaggerConfiguration {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_HEADER_DESCRIPTION = "令牌";

    @Bean
    @ConditionalOnMissingBean
    public SwaggerProp swaggerProperties() {
        return new SwaggerProp();
    }

    @Bean(value = "defaultApi2")
    public Docket defaultApi2(SwaggerProp properties) {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name(AUTHORIZATION_HEADER).description(AUTHORIZATION_HEADER_DESCRIPTION).modelRef(new ModelRef(
                "string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(properties))
                .groupName(properties.getVersion()).select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any()).build()
                .globalOperationParameters(pars);

    }

    private ApiInfo apiInfo(SwaggerProp prop) {
        return new ApiInfoBuilder()
                .title(prop.getTitle())
                .description(prop.getDescription())
                .termsOfServiceUrl(prop.getTermsOfServiceUrl())
                .version(prop.getVersion())
                .build();
    }
}
