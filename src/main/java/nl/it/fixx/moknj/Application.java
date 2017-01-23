package nl.it.fixx.moknj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Fuck I made this, all of it! Okay to the brass tax, moknJ was developed in 3
 * and half months with few late nights included to build the concept, that said
 * this project has few places where one can find bad code decision mostly every
 * where, but i would like to who ever takes this project over to use BAL aka
 * BusinessAccessLayer to do any bs which would have been done in rest
 * controllers. The rest controllers are just there to execute basic logic calls
 * as call a BAL and execute its bs...
 *
 * See the BusinessAccessLayer for description of the layer and have a look at
 * UserAccessBal which has the desired way of using a BAL.
 *
 *
 *
 *
 * @author adriaan
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
