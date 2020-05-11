package com.hardlight.auxiliary;

import com.hardlight.bots.interfaces.Bot;
import com.hardlight.bots.interfaces.MultiplayerBot;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class SnakeGameLoader {
    public static final String BOTS_PACKAGE = "com.hardlight.bots.implementations";
    public static final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    public static final URL root = classLoader.getResource(BOTS_PACKAGE.replace(".", "/"));

    public static Bot[] getBotClasses() {
        return getClassesStream(Bot.class).toArray(Bot[]::new);
    }

    public static MultiplayerBot[] getMultiplayerBotClasses() {
        return getClassesStream(MultiplayerBot.class).toArray(MultiplayerBot[]::new);
    }

    public static <T> Stream<? extends T> getClassesStream(Class<T> interfaceClass) {
        try {
            assert root != null;
            Path[] filenames = Files.walk(Paths.get(root.toURI()))
                    .filter(Files::isRegularFile)
                    .toArray(Path[]::new);

            return Arrays.stream(filenames)
                    .parallel()
                    .map(Path::toString)
                    .map(filename -> filename
                            .replaceAll(".+" + BOTS_PACKAGE, "")
                            .replaceAll(".class$", "")
                            .replaceAll("/", "."))
                    .map(className -> {
                        try {
                            return Class.forName(BOTS_PACKAGE + className);
                        } catch (ClassNotFoundException ignore) {
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .filter(loadedClass -> Arrays.asList(loadedClass.getInterfaces()).contains(interfaceClass))
                    .map(loadedClass -> (Class<? extends T>) loadedClass.asSubclass(interfaceClass))
                    .map(botClassConstructor -> {
                        try {
                            return botClassConstructor.getDeclaredConstructor().newInstance();
                        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException ignore) {
                        }
                        return null;
                    })
                    .filter(Objects::nonNull);
        } catch (AssertionError | IOException | URISyntaxException error) {
            error.printStackTrace();
        }

        return Stream.empty();
    }

    public static void main(String[] args) {
        System.out.println(getBotClasses().length);
        System.out.println(getMultiplayerBotClasses().length);
    }
}
