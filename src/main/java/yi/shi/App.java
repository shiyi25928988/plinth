package yi.shi;

import yi.shi.plinth.annotation.PropertiesFile;
import yi.shi.plinth.boot.ServiceBooter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Hello world!
 *
 */
@PropertiesFile(files = { "application.properties" })
@Slf4j
public class App 
{
    public static void main(String... strings) {
        try {
            ServiceBooter.startFrom(App.class);
        } catch (ClassNotFoundException | IOException e) {
            log.error(e.getMessage());
        }
    }
}
