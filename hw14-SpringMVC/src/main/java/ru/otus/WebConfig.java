package ru.otus;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.dao.ClientDaoImpl;
import ru.otus.core.dao.RoleDao;
import ru.otus.core.dao.RoleDaoImpl;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.Client;
import ru.otus.core.model.PhoneData;
import ru.otus.core.model.Role;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

@Configuration
@ComponentScan
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public WebConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(true);
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setOrder(1);
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }

    @Bean   
    public ClientDao clientDao() {
        return new ClientDaoImpl(sessionManager());
    }

    @Bean
    public RoleDao roleDao(){
        return new RoleDaoImpl(sessionManager());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/no-handler-view").setViewName("noHandlerView");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/WEB-INF/static/");
    }

    @Bean
    public SessionManagerHibernate sessionManager(){
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration().configure(HIBERNATE_CFG_FILE);
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, PhoneData.class, AddressDataSet.class, Role.class);
        return new SessionManagerHibernate(sessionFactory);
    }

}
