package utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FindAll {
    private static final char PKG_SEPARATOR = '.';

    private static final char DIR_SEPARATOR = '/';

    private static final String CLASS_FILE_SUFFIX = ".txt";

    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";
    private static String PACKAGE = "";

    public static HashMap<String, String> find(String scannedPackage) {
        PACKAGE = scannedPackage;
        String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        System.out.println(scannedUrl);
        if (scannedUrl == null) {
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
        }
        File scannedDir = new File(scannedUrl.getFile());
        HashMap<String, String> storageString = new HashMap<>();
        for (File file : scannedDir.listFiles()) {
            storageString.putAll(findClass(file, scannedPackage));
        }
        return storageString;
    }

    //TODO сделать как нибудь красивше.
    private static HashMap<String, String> findClass(File file, String scannedPackage) {
        HashMap<String, String> classes = new HashMap<>();
        String resource = scannedPackage + PKG_SEPARATOR + file.getName();
        if (file.isDirectory()) {
            // если в этой папке будут еще папки
            for (File child : file.listFiles()) {
                classes.putAll(findClass(child, resource));
            }
        } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
            int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
            int startIndex = (PACKAGE + PKG_SEPARATOR).length();
            String key = resource.substring(startIndex, endIndex);
            String values = "";
            try {
                values = Files.lines(Paths.get(file.getPath()))
                        //TODO написать регулярку если надо будет
//                        .map(everyLine -> everyLine.split("require ‘"))
//                        .flatMap(Arrays::stream)
                        .collect(Collectors.joining());
            } catch (IOException e) {
                e.printStackTrace();
            }
            classes.put(key, values);
        }
        return classes;
    }
}
