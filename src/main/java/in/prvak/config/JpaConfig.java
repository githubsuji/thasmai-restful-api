package in.prvak.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author SUJIKUMAR
 * Reference: [ http://websystique.com/spring-boot/spring-boot-angularjs-spring-data-jpa-crud-app-example/,
 * https://www.thomasvitale.com/spring-data-jpa-hibernate-java-configuration/, 
 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#new-features.1-11-0,
 * https://hellokoding.com/registration-and-login-example-with-spring-security-spring-boot-spring-data-jpa-hsql-jsp/
 * https://www.mkyong.com/spring/spring-propertysources-example/
 * https://www.mkyong.com/spring-boot/spring-boot-configurationproperties-example/
]
 * 
 */
@Configuration
@EnableJpaRepositories(basePackages = {"in.prvak.repository","com.thasmai.repository","com.thasmai.controller"}, entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager")
@EnableTransactionManagement
@PropertySource("classpath:jpa/jpa.properties")
public class JpaConfig {
  @Autowired
  private Environment env;
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setDatabase(Database.HSQL);
   // vendorAdapter.setDatabase(Database.MYSQL);
    vendorAdapter.setGenerateDdl(true);
    
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource());
   em.setPackagesToScan("in.prvak.jpa.entities","com.thasmai.jpa.entities");
    em.setJpaVendorAdapter(vendorAdapter);
    em.setJpaProperties(additionalProperties());
 
    return em;
  }
  
  @Bean
  public DataSource dataSource(){
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(env.getProperty("hibernate.connection.driver_class"));
    dataSource.setUrl(env.getProperty("hibernate.connection.url"));
    dataSource.setUsername(env.getProperty("hibernate.connection.username"));
    dataSource.setPassword(env.getProperty("hibernate.connection.password"));
    return dataSource;
  }
  
  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(emf);
    return transactionManager;
  }
  
  @Bean
  public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
    return new PersistenceExceptionTranslationPostProcessor();
  }
  Properties additionalProperties() {
    Properties properties = new Properties();
    properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
    properties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
    properties.setProperty("hibernate.current_session_context_class", env.getProperty("hibernate.current_session_context_class"));
    properties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
    properties.setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
    properties.setProperty("hibernate.validator.autoregister_listeners", env.getProperty("hibernate.validator.autoregister_listeners"));
    properties.setProperty("hibernate.validator.apply_to_ddl", env.getProperty("hibernate.validator.apply_to_ddl"));
    properties.setProperty("hibernate.schema_update.unique_constraint_strategy", env.getProperty("hibernate.schema_update.unique_constraint_strategy"));

    return properties;
  }
}
