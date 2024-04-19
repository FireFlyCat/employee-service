package com.bigcompany.config;

import com.bigcompany.annotation.Inject;
import com.bigcompany.exception.UnexpectedException;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

/**
 * JavaConfig class implements simple Dependency Inversion logic and controls object creation of the application.
 * @implNote current implementation does not support more than 1 implementation of the interface,
 * has cyclic dependency vulnerability and does not support Mockito mocking and stubbing on a fly
 * */
public class JavaConfig {

    private static JavaConfig factory;

    private final ConcurrentHashMap<Class, Object> classMap;

    private JavaConfig() {
        classMap = new ConcurrentHashMap<>();
    }

    public JavaConfig(ConcurrentHashMap<Class, Object> classMap) {
        this.classMap = classMap;
    }

    /**
     * Gets the instance of the current JavaConfig
     * */
    public static JavaConfig getInstance() {
        if (factory == null) {
            factory = new JavaConfig();
        }
        return factory;
    }
    /**
     * Recreates the JavaConfig with objects to override
     * @implNote This is a workaround to simplify DI logic in JavaConfig and to support custom implementations.
     * should be removed in future releases
     * @param classMap has a map of objects to override default implementation
     * */
    public static JavaConfig init(Map<Class, Object> classMap) {
        factory = new JavaConfig(new ConcurrentHashMap<>(classMap));
        return factory;
    }

    /**
     * Recursively initializes and returns object of the Class. Initializes properties marked as <code>@Inject</code>
     * @param clazzToReturn object class to return
     * */
    public <T> T getObject(Class<T> clazzToReturn) {
        if (classMap.get(clazzToReturn) != null) {
            return (T) classMap.get(clazzToReturn);
        }

        try {
            T object = clazzToReturn.getDeclaredConstructor().newInstance();
            classMap.put(clazzToReturn, object);

            for (Field field : clazzToReturn.getDeclaredFields()) {
                Inject annotation = field.getAnnotation(Inject.class);
                if (annotation != null) {
                    Class<?> declaringClass = field.getType();
                    field.setAccessible(true);
                    field.set(object, getObject(declaringClass));
                }
            }

            return object;
        } catch (Exception e) {
            throw new UnexpectedException(format("Error initializing object %s", clazzToReturn.getName()), e);
        }
    }

}
