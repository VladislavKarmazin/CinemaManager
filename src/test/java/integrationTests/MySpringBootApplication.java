package integrationTests;

import controller.MoviesServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MySpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySpringBootApplication.class, args);
    }

    @Bean
    public ServletRegistrationBean<MoviesServlet> groupServletRegistrationBean() {
        ServletRegistrationBean<MoviesServlet> registrationBean = new ServletRegistrationBean<>(new MoviesServlet());
        registrationBean.setName("MoviesServlet");
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/movie");
        return registrationBean;
    }



//     Мой контекст, никак не связан с контекстом спринга
//    @Bean
//    public WebApplicationContext webApplicationContext() {
//        WebApplicationContext webApplicationContext = new WebApplicationContext();
//        webApplicationContext.setObjectContainerBuilder(testObjectContainerBuilder());
//        return webApplicationContext;
//    }

//    private ObjectContainerBuilder testObjectContainerBuilder() {
//        return new ProductionObjectContainerBuilder() {
//            @Override
//            protected void configure() {
//                add(testJdbcConnectionFactory(), JdbcConnectionFactory.class);
//                super.configure();
//            }
//        };
//    }

//    @Bean
//    public JdbcConnectionFactory testJdbcConnectionFactory() {
//        return new JdbcConnectionFactory("org.h2.Driver", "jdbc:h2:mem:test_db;DEFAULT_LOCK_TIMEOUT=10000;LOCK_MODE=0;DB_CLOSE_DELAY=-1");
//    }
//
//    @Bean
//    public TestJdbcHelper testJdbcHelper() {
//        return new TestJdbcHelper(testJdbcConnectionFactory());
//    }
}