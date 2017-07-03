package cc.jcsw.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@ImportResource("classpath:applicationContext.xml")
public class BootApp extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication sa = new SpringApplication(BootApp.class);
        sa.addListeners(new ApplicationPidFileWriter("d:\\tmp\\bob1.txt"));
        sa.run(args);
        // SpringApplication.run(BootApp.class);
    }

}
