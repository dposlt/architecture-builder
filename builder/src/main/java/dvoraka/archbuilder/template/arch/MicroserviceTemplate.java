package dvoraka.archbuilder.template.arch;

import dvoraka.archbuilder.DirType;
import dvoraka.archbuilder.Directory;
import dvoraka.archbuilder.generate.JavaUtils;
import dvoraka.archbuilder.sample.microservice.data.BaseException;
import dvoraka.archbuilder.sample.microservice.data.ResultData;
import dvoraka.archbuilder.sample.microservice.data.message.ResponseMessage;
import dvoraka.archbuilder.sample.microservice.net.ServiceNetworkComponent;
import dvoraka.archbuilder.template.config.BuildGradleTemplate;
import dvoraka.archbuilder.template.config.ConfigurationTemplate;
import dvoraka.archbuilder.template.config.SettingsGradleTemplate;
import dvoraka.archbuilder.template.source.SourceTemplate;
import dvoraka.archbuilder.template.source.SpringBootApplicationTemplate;

import java.util.List;

public class MicroserviceTemplate implements ArchitectureTemplate {

    public static final String JAVA_SRC_DIR = "src/main/java";

    private Directory root;
    private Directory srcRoot;
    private Directory srcBase;
    private Directory srcBaseAbs;


    public MicroserviceTemplate(
            String rootDirName,
            String packageName,
            Class<?> superService,
            List<Class<?>> typeArguments,
            String serviceName,
            Class<?> superServer,
            Class<?> requestSuperMessage
    ) {
        root = new Directory.DirectoryBuilder(rootDirName)
                .type(DirType.ROOT)
                .parent(null)
                .build();

        srcRoot = new Directory.DirectoryBuilder(JAVA_SRC_DIR)
                .type(DirType.SRC_ROOT)
                .parent(root)
                .build();

        String pkgPath = JavaUtils.pkg2path(packageName);
        srcBase = new Directory.DirectoryBuilder(pkgPath)
                .type(DirType.SRC_BASE)
                .parent(srcRoot)
                .build();

        String absPkgPath = "";
        srcBaseAbs = new Directory.DirectoryBuilder(absPkgPath)
                .type(DirType.SRC_BASE_ABSTRACT)
                .parent(root)
                .build();

        // service
        Directory abstractService = new Directory.DirectoryBuilder("service")
                .type(DirType.SERVICE_ABSTRACT)
                .parent(srcBaseAbs)
                .typeClass(superService)
                .build();

        Directory.DirectoryBuilder serviceBuilder = new Directory.DirectoryBuilder("service")
                .type(DirType.SERVICE)
                .parent(srcBase)
                .superType(abstractService)
                .filename(serviceName);
        for (Class<?> typeArgument : typeArguments) {
            serviceBuilder.parameterTypeClass(typeArgument);
        }
        Directory service = serviceBuilder
                .build();

        Directory serviceImpl = new Directory.DirectoryBuilder("service")
                .type(DirType.SERVICE_IMPL)
                .parent(srcBase)
                .superType(service)
                .build();

        // data
        Directory dataAbs = new Directory.DirectoryBuilder("")
                .type(DirType.ABSTRACT)
                .parent(srcBase)
                .typeClass(ResultData.class)
                .build();

        String dataName = "TestingData";
        Directory data = new Directory.DirectoryBuilder("data")
                .type(DirType.IMPL)
                .parent(srcBase)
                .superType(dataAbs)
                .filename(dataName)
                .parameterTypeClass(BaseException.class)
                .build();

        // messages
        Directory requestMessageAbs = new Directory.DirectoryBuilder("")
                .type(DirType.ABSTRACT)
                .parent(srcBase)
                .typeClass(requestSuperMessage)
                .build();

        String requestMessageName = serviceName + "Message";
        Directory requestMessage = new Directory.DirectoryBuilder("data/message")
                .type(DirType.IMPL)
                .parent(srcBase)
                .superType(requestMessageAbs)
                .parameterTypeDir(service)
                .filename(requestMessageName)
                .build();

        Directory responseMessageAbs = new Directory.DirectoryBuilder("")
                .type(DirType.ABSTRACT)
                .parent(srcBase)
                .typeClass(ResponseMessage.class)
                .build();

        String responseMessageName = serviceName + "ResponseMessage";
        Directory responseMessage = new Directory.DirectoryBuilder("data/message")
                .type(DirType.IMPL)
                .parent(srcBase)
                .superType(responseMessageAbs)
                .parameterTypeDir(data)
                .parameterTypeClass(BaseException.class)
                .filename(responseMessageName)
                .build();

        // server
        Directory serverAbs = new Directory.DirectoryBuilder("")
                .type(DirType.ABSTRACT)
                .parent(srcBase)
                .typeClass(superServer)
                .build();

        String serverName = serviceName + "Server";
        Directory server = new Directory.DirectoryBuilder("server")
                .type(DirType.IMPL)
                .parent(srcBase)
                .superType(serverAbs)
                .filename(serverName)
                .build();
        // network component
        Directory networkComponentAbs = new Directory.DirectoryBuilder("")
                .type(DirType.ABSTRACT)
                .parent(srcBase)
                .typeClass(ServiceNetworkComponent.class)
                .build();

        String networkComponentName = serviceName + "NetworkComponent";
        Directory networkComponent = new Directory.DirectoryBuilder("server")
                .type(DirType.IMPL)
                .parent(srcBase)
                .superType(networkComponentAbs)
                .interfaceType()
                .parameterTypeDir(requestMessage)
                .parameterTypeDir(responseMessage)
                .filename(networkComponentName)
                .build();

        // application properties
        Directory srcProps = new Directory.DirectoryBuilder("src/main/resources")
                .type(DirType.SRC_PROPERTIES)
                .parent(root)
                .filename("application.properties")
                .text("prop1=value\nprop2=value2\n")
                .build();

        // application
        String appClassName = serviceName + "App";
        SourceTemplate sourceTemplate = new SpringBootApplicationTemplate(appClassName, packageName);
        Directory application = new Directory.DirectoryBuilder("")
                .type(DirType.CUSTOM_TYPE)
                .parent(srcBase)
                .filename(appClassName)
                .text(sourceTemplate.getSource())
                .build();

        // build configuration
        ConfigurationTemplate buildGradleTemplate = new BuildGradleTemplate();
        Directory buildGradle = new Directory.DirectoryBuilder("")
                .type(DirType.BUILD_CONFIG)
                .parent(root)
                .filename(buildGradleTemplate.getFilename())
                .text(buildGradleTemplate.getConfig())
                .build();
        ConfigurationTemplate settingsGradleTemplate = new SettingsGradleTemplate("Project1");
        Directory settingsGradle = new Directory.DirectoryBuilder("")
                .type(DirType.BUILD_CONFIG)
                .parent(root)
                .filename(settingsGradleTemplate.getFilename())
                .text(settingsGradleTemplate.getConfig())
                .build();
    }

    @Override
    public Directory getRootDirectory() {
        return root;
    }
}
