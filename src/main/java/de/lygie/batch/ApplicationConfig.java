package de.lygie.batch;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class ApplicationConfig extends Application {
    // Weitere Konfigurationen sind optional

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(BatchJobResource.class);  // Stelle sicher, dass der Klassenname korrekt ist
        resources.add(DefaultResource.class);
        return resources;
    }
}
