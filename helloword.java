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
            app.setDefaultProperties(java.util.Collections.singletonMap("server.port", String.valueOf(port)));
            app.run();
            return 0;
        }
        java.lang.System.out.println(
                com.github.lalyos.jfiglet.FigletFont.convertOneLine(new java.io.File("./Doom.flf"), "Hello " + this.greeting));
        return 0;
    }

    @SpringBootApplication
    public static class SpringBootApp {
    }
}
