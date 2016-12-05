package context;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Никита on 05.12.2016.
 */
public class ClassFinder {

    private static char dotSeparator = '.';
    private static char slashSeparator= '/';
    public static List<Class<?>> getClassesFromPackage(String packageName){

        //дописать проверку и обработку исключений
        String path = packageName.replace(dotSeparator, slashSeparator);
        URL scannedUrl = ClassLoader.getSystemClassLoader().getResource(path);

        File scannedDir = new File(scannedUrl.getFile());
        List<Class<?>> classes = new ArrayList<Class<?>>();
        for (File file : scannedDir.listFiles()) {
            classes.addAll(find(file, packageName));
        }
        return classes;

    }

    private static List<Class<?>> find(File file, String packageName) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        String resource = packageName + dotSeparator+ file.getName();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(".class")) {
            int endIndex = resource.length() - ".class".length();
            String className = resource.substring(0, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ignore) {
            }
        }
        return classes;
    }
}
