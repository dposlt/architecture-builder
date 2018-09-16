package dvoraka.architecturebuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {

    @Autowired
    private DirService dirService;
    @Autowired
    private LangGenerator langGenerator;
    @Autowired
    private Generator generator;


    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public CommandLineRunner runner() {
        return args -> {

            Directory root = new Directory.DirectoryBuilder("rootDir")
                    .withType(DirType.ROOT)
                    .withParent(null)
                    .build();

            Directory srcRoot = new Directory.DirectoryBuilder("src/main/java")
                    .withType(DirType.SRC_ROOT)
                    .withParent(root)
                    .build();

            Directory srcTestRoot = new Directory.DirectoryBuilder("src/test/groovy")
                    .withType(DirType.SRC_TEST_ROOT)
                    .withParent(root)
                    .build();

            Directory srcBase = new Directory.DirectoryBuilder("dvoraka/testapp")
                    .withType(DirType.SRC_BASE)
                    .withParent(srcRoot)
                    .build();

            Directory srcProps = new Directory.DirectoryBuilder("src/main/resources")
                    .withType(DirType.SRC_PROPERTIES)
                    .withParent(root)
                    .withFilename("application.properties")
                    .build();

            Directory srcTestProps = new Directory.DirectoryBuilder("src/test/resources")
                    .withType(DirType.SRC_PROPERTIES)
                    .withParent(root)
                    .withFilename("application.properties")
                    .build();

            Directory srcBaseAbs = new Directory.DirectoryBuilder("dvoraka/diffapp")
                    .withType(DirType.SRC_BASE_ABSTRACT)
                    .withParent(root)
                    .build();

            Directory srcTestBase = new Directory.DirectoryBuilder("dvoraka/testapp")
                    .withType(DirType.SRC_TEST_BASE)
                    .withParent(srcTestRoot)
                    .build();

            Directory serviceAbs = new Directory.DirectoryBuilder("service")
                    .withType(DirType.SERVICE_ABSTRACT)
                    .withParent(srcBaseAbs)
//                    .withFilename("java.security.AlgorithmConstraints")
//                    .withFilename("java.lang.Runnable")
//                    .withFilename("java.util.concurrent.RunnableFuture")
                    .withFilename("java.util.Map")
//                    .withFilename("org.springframework.context.MessageSource")
                    .build();

            Directory service = new Directory.DirectoryBuilder("service")
                    .withType(DirType.SERVICE)
                    .withParent(srcBase)
                    .withFilename("CoolService")
                    .build();

            Directory serviceImpl = new Directory.DirectoryBuilder("service")
                    .withType(DirType.SERVICE_IMPL)
                    .withParent(srcBase)
                    .dependsOn(service)
                    .addParameterType("java.lang.Integer")
                    .addParameterType("java.lang.String")
                    .build();

            generator.generate(root);
        };
    }
}
