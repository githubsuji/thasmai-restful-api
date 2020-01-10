package in.prvak.config.web;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

 

@Configuration
//@EnableWebMvc
public class DefaultView implements WebMvcConfigurer{

 

    @Override
    public void addViewControllers( ViewControllerRegistry registry ) {

        registry.addViewController( "/" ).setViewName( "forward:/index.html" );

        registry.setOrder( Ordered.HIGHEST_PRECEDENCE );

        

    }
    
    @Bean
    public DeviceResolverHandlerInterceptor 
            deviceResolverHandlerInterceptor() {
        return new DeviceResolverHandlerInterceptor();
    }

    @Bean
    public DeviceHandlerMethodArgumentResolver 
            deviceHandlerMethodArgumentResolver() {
        return new DeviceHandlerMethodArgumentResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(deviceResolverHandlerInterceptor());
    }

    @Override
    public void addArgumentResolvers(
           List<HandlerMethodArgumentResolver> argumentResolvers) {
       argumentResolvers.add(deviceHandlerMethodArgumentResolver());
    }
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    } 
//    @Bean
//    public DeviceResolverHandlerInterceptor 
//    deviceResolverHandlerInterceptor() {
//return new DeviceResolverHandlerInterceptor();
//}
//
//@Bean
//public DeviceHandlerMethodArgumentResolver 
//    deviceHandlerMethodArgumentResolver() {
//return new DeviceHandlerMethodArgumentResolver();
//}
//
//@Override
//public void addInterceptors(InterceptorRegistry registry) {
//registry.addInterceptor(deviceResolverHandlerInterceptor());
//}
//
//@Override
//public void addArgumentResolvers(
//    List<HandlerMethodArgumentResolver> argumentResolvers) {
//argumentResolvers.add(deviceHandlerMethodArgumentResolver());
//}
}
