package dvoraka.archbuilder.template;

import dvoraka.archbuilder.data.DirType;
import dvoraka.archbuilder.data.Directory;
import dvoraka.archbuilder.template.text.TextFileTemplate;

public interface TemplateHelper {

    String JAVA_SRC_DIR = "src/main/java";


    default Directory root(String rootDirName) {
        return new Directory.Builder(rootDirName, DirType.ROOT)
                .parent(null)
                .build();
    }

    default Directory srcRoot(Directory root) {
        return new Directory.Builder(TemplateHelper.JAVA_SRC_DIR, DirType.SRC_ROOT)
                .parent(root)
                .build();
    }

    default Directory srcBase(Directory srcRoot, String pkgPath) {
        return new Directory.Builder(pkgPath, DirType.SRC_BASE)
                .parent(srcRoot)
                .build();
    }

    default Directory srcRootAndBase(Directory root, String pkgPath) {
        return srcBase(srcRoot(root), pkgPath);
    }

    default Directory srcAbsBase(Directory root, String pkgPath) {
        return new Directory.Builder(pkgPath, DirType.SRC_BASE_ABSTRACT)
                .parent(root)
                .build();
    }

    default Directory textFile(Directory parent, TextFileTemplate template) {
        return new Directory.Builder(template.getPath(), DirType.TEXT)
                .parent(parent)
                .filename(template.getFilename())
                .text(template.getText())
                .build();
    }

    default Directory classFile(Directory parent, TextFileTemplate template) {
        return new Directory.Builder(template.getPath(), DirType.CUSTOM_TYPE)
                .parent(parent)
                .filename(template.getFilename())
                .text(template.getText())
                .build();
    }

    default Directory buildGradle(Directory root, TextFileTemplate buildGradle) {
        return textFile(root, buildGradle);
    }

    default Directory settingsGradle(Directory root, TextFileTemplate settingsGradle) {
        return textFile(root, settingsGradle);
    }

    default Directory gitignore(Directory root, TextFileTemplate template) {
        return textFile(root, template);
    }

    default Directory properties(Directory root, TextFileTemplate template) {
        return textFile(root, template);
    }

    default Directory springBootApp(Directory root, TextFileTemplate template) {
        return classFile(root, template);
    }

    default String buildServiceName(String serviceName) {
        return serviceName + "Service";
    }

    default String buildServiceImplName(String serviceName) {
        return "Default" + buildServiceName(serviceName);
    }

    default String buildServiceConfigurationName(String serviceName) {
        return serviceName + "Config";
    }

    default String buildServiceAppName(String serviceName) {
        return serviceName + "App";
    }

    default String buildExceptionName(String baseName) {
        return baseName + "Exception";
    }

    default String buildServerName(String baseName) {
        return baseName + "Server";
    }

    default String buildConfigurationName(String baseName) {
        return baseName + "Config";
    }

    default String buildServiceControllerName(String serviceName) {
        return serviceName + "Controller";
    }
}
