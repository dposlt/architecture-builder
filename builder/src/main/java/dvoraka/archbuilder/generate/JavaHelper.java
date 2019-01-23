package dvoraka.archbuilder.generate;

import com.squareup.javapoet.TypeVariableName;
import dvoraka.archbuilder.Directory;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface JavaHelper {

    default List<Method> findMethods(Class<?> clazz) {

        // find protected methods
        List<Method> protectedMethods = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> Modifier.isProtected(method.getModifiers()))
                .collect(Collectors.toList());

        // find public methods
        List<Method> publicMethods = Arrays.asList(clazz.getMethods());

        List<Method> allMethods = new ArrayList<>();
        allMethods.addAll(protectedMethods);
        allMethods.addAll(publicMethods);

        return allMethods;

//        return Arrays.asList(clazz.getMethods());

//        if (clazz.getInterfaces().length == 0) {
//            return Arrays.asList(clazz.getDeclaredMethods());
//        }
//
//        List<Method> methods = new ArrayList<>();
//        for (Class<?> cls : clazz.getInterfaces()) {
//            methods.addAll(findMethods(cls));
//        }
//
//        return methods;
    }

    default String getReturnValue(Type returnType) {
        String returnValue = "XXX";

        if (returnType instanceof Class
                && !((Class) returnType).isPrimitive()) {
            returnValue = "null";
        } else if (returnType instanceof ParameterizedType) {
            returnValue = "null";
        } else if (returnType instanceof TypeVariable) {
            returnValue = "null";
        } else if (returnType instanceof GenericArrayType) {
            returnValue = "null";
        } else if (returnType == Boolean.TYPE) {
            returnValue = "false";
        } else if (returnType == Character.TYPE) {
            returnValue = "'\n'";
        } else if (returnType == Double.TYPE) {
            returnValue = "0.0";
        } else if (returnType == Float.TYPE) {
            returnValue = "0.0f";
        } else if (returnType == Integer.TYPE || returnType == Short.TYPE || returnType == Byte.TYPE) {
            returnValue = "0";
        } else if (returnType == Long.TYPE) {
            returnValue = "0L";
        }

        return returnValue;
    }

    default Class<?> loadClass(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

    default String javaSuffix(String filename) {
        return filename + ".java";
    }

    default String getClassName(Directory directory) {
        return JavaUtils.getClassName(directory);
    }

    default String getPathString(Directory directory, String filename) {
        return directory.getPath() + File.separator + filename;
    }

    default String defaultServiceImplName(Directory serviceImpl) {
        return new StringBuilder()
                .append(serviceImpl.getPackageName())
                .append(".")
                .append("Default")
                .append(serviceImpl.getFilename()
                        .orElseThrow(() -> new RuntimeException("No filename specified!")))
                .toString();
    }

    default void addClassPath(Path path) {
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(ClassLoader.getSystemClassLoader(), path.toUri().toURL());
        } catch (NoSuchMethodException
                | IllegalAccessException
                | InvocationTargetException
                | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    default RuntimeException noSuperTypeException() {
        return new RuntimeException("No super type specified!");
    }

    default boolean isConstructorNeeded(Class<?> superClass) {

        Constructor<?>[] declaredConstructors = superClass.getDeclaredConstructors();
        if (declaredConstructors.length == 0) {
            return false;
        }

        return Arrays.stream(declaredConstructors)
                .noneMatch(constructor -> constructor.getParameterCount() == 0);
    }

    default List<TypeVariableName> getTypeVariableNames(Class<?> clazz) {

        return Stream.of(clazz.getTypeParameters())
                .map(TypeVariableName::get)
                .collect(Collectors.toList());
    }

    default String buildSuperString(String[] argNames) {
        if (argNames == null || argNames.length == 0) {
            throw new IllegalArgumentException();
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("super(");
        for (int i = 0; i < argNames.length; i++) {
            stringBuilder.append("%s");
            if (i != (argNames.length - 1)) { // not last
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    default Type[] buildTypeArray(
            TypeVariable<? extends Class<?>>[] typeParameters,
            Map<TypeVariable<?>, Type> typeMapping
    ) {
        List<Type> types = new ArrayList<>();
        for (TypeVariable<? extends Class<?>> typeVariable : typeParameters) {
            Type realType = typeMapping.get(typeVariable);
            types.add(realType);
        }

        return types.toArray(new Type[0]);
    }
}