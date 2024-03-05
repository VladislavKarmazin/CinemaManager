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
}