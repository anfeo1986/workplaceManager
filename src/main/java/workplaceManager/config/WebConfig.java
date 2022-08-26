package workplaceManager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "workplaceManager")
@PropertySource(value = "classpath:server.properties")
public class WebConfig implements WebMvcConfigurer {

    private Environment environment;
    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String cssStr = environment.getRequiredProperty("resource.css");
        registry.addResourceHandler(cssStr + "**").addResourceLocations(cssStr);
    }

    @Bean
    ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix(environment.getRequiredProperty("resolver.prefix"));
        viewResolver.setSuffix(environment.getRequiredProperty("resolver.suffix"));
        return viewResolver;
    }
}
