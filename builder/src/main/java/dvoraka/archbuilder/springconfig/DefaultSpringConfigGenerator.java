package dvoraka.archbuilder.springconfig;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import dvoraka.archbuilder.data.Directory;
import dvoraka.archbuilder.exception.GeneratorException;
import dvoraka.archbuilder.generate.JavaHelper;
import dvoraka.archbuilder.util.JavaUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultSpringConfigGenerator implements SpringConfigGenerator, JavaHelper {

    @Override
    public String genConfiguration(List<BeanMapping> beanMappings, Directory dir) {

        List<MethodSpec> methodSpecs = new ArrayList<>();
        for (BeanMapping mapping : beanMappings) {

            Class<?> mappingClass;
            mappingClass = mapping.getTypeDir() != null
                    ? loadClass(mapping.getTypeDir().getTypeName())
                    : mapping.getType();

            Object code = mapping.getCodeTemplate() != null
                    ? mapping.getCodeTemplate().apply(mapping)
                    : mapping.getCode();

            //TODO: add String code
            CodeBlock codeBlock;
            if (code instanceof CodeBlock) {
                codeBlock = (CodeBlock) code;
            } else {
                throw new GeneratorException("Code template must has type CodeBlock.");
            }

            List<ParameterSpec> parameterSpecs = new ArrayList<>();
            for (BeanParameter parameter : mapping.getParameters()) {
                Class<?> parameterClass;
                parameterClass = parameter.getTypeDir() != null
                        ? loadClass(parameter.getTypeDir().getTypeName())
                        : parameter.getType();

                parameterSpecs.add(ParameterSpec.builder(
                        parameterClass,
                        parameter.getName())
                        .build()
                );
            }

            MethodSpec methodSpec = MethodSpec.methodBuilder(mapping.getName())
                    .addAnnotation(Bean.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(mappingClass)
                    .addParameters(parameterSpecs)
                    .addStatement(codeBlock)
                    .build();

            methodSpecs.add(methodSpec);
        }

        String filename = dir.getFilename()
                .orElse("SpringConfig");
        String typeName = JavaUtils.removeJavaSuffix(filename);

        TypeSpec spec = TypeSpec.classBuilder(typeName)
                .addAnnotation(Configuration.class)
                .addMethods(methodSpecs)
                .build();
        JavaFile javaFile = JavaFile.builder(dir.getPackageName(), spec)
                .build();

        return javaFile.toString();
    }

    @Override
    public CodeBlock simpleReturn(BeanMapping beanMapping) {

        Class<?> returnClass = getReturnClass(beanMapping);

        return CodeBlock.of(
                "return new $T()",
                returnClass
        );
    }

    @Override
    public CodeBlock paramReturn(BeanMapping beanMapping) {

        Class<?> returnClass = getReturnClass(beanMapping);

        StringBuilder templateBuilder = new StringBuilder("return new $T(");
        List<BeanParameter> parameters = beanMapping.getParameters();

        for (int i = 0; i < parameters.size(); i++) {
            templateBuilder.append("$L");
            if (i != parameters.size() - 1) {
                templateBuilder.append(", ");
            }
        }
        templateBuilder.append(")");

        List<Object> templateParameters = new ArrayList<>();
        templateParameters.add(returnClass);
        templateParameters.addAll(parameters.stream()
                .map(BeanParameter::getName)
                .collect(Collectors.toList()));

        return CodeBlock.of(
                templateBuilder.toString(),
                templateParameters.toArray()
        );
    }

    private Class<?> getReturnClass(BeanMapping beanMapping) {
        return beanMapping.getToTypeDir() != null
                ? loadClass(beanMapping.getToTypeDir().getTypeName())
                : beanMapping.getToType();
    }
}
