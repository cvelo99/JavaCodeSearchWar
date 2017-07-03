package cc.jcsw.beans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("server")
public class PidWriter implements ApplicationListener<ApplicationStartingEvent> {

    @Value("${writePid}")
    private boolean writePid;

    @Override
    public void onApplicationEvent(ApplicationStartingEvent event) {
        ApplicationPidFileWriter listener = new ApplicationPidFileWriter("d:\\tmp\\bob.txt");
        listener.setTriggerEventType(ApplicationReadyEvent.class);
        event.getSpringApplication().addListeners(listener);
    }
}
