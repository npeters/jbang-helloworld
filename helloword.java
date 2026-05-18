///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.5.0 info.picocli:picocli-codegen:4.6.1
//DEPS com.github.lalyos:jfiglet:0.0.8
//DEPS org.springframework.boot:spring-boot-starter-web:2.7.18
//FILES Doom.flf
//JAVA 16

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;
import static java.lang.System.*;
import java.util.concurrent.Callable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.github.lalyos.jfiglet.FigletFont;
import java.io.File;
import java.util.Collections;

@Command(name = "helloword", mixinStandardHelpOptions = true, version = "helloword 0.1", description = "helloword made with jbang")
class helloword implements Callable<Integer> {

    @Parameters(index = "0", description = "The greeting to print", defaultValue = "World!")
    private String greeting;

    @Option(names = "--server", description = "Start HTTP server mode")
    private boolean server;

    @Option(names = "--port", description = "Port for HTTP server (default: 8080)")
    private int port = 8080;

    public static void main(String... args) {
        int exitCode = new CommandLine(new helloword()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        if (server) {
            SpringApplication app = new SpringApplication(SpringBootApp.class);
            app.setDefaultProperties(Collections.singletonMap("server.port", String.valueOf(port)));
            app.run();
            return 0;
        }
        out.println(FigletFont.convertOneLine(new File("./Doom.flf"), "Hello " + greeting));
        return 0;
    }

    @SpringBootApplication
    public static class SpringBootApp {
    }
}

@RestController
class HelloController {

    @GetMapping("/hello")
    public Greeting hello(@RequestParam(name = "name", defaultValue = "World") String name) {
        return new Greeting("Hello " + name + "!");
    }

    static class Greeting {
        private final String message;

        Greeting(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
