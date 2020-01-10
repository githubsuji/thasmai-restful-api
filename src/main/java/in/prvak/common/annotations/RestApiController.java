package in.prvak.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RestController
@RequestMapping
public @interface RestApiController {

    @AliasFor(annotation = RequestMapping.class, attribute = "value")
    String[] value() default "";

}
//https://jira.spring.io/browse/SPR-16336
//https://github.com/mh-dev/blog_rest-controller-base-path
//https://mhdevelopment.wordpress.com/2016/10/03/spring-restcontroller-specific-basepath/