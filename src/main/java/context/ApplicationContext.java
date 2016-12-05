package context;

import annotation.Autowired;
import annotation.ComponentA;
import com.sun.scenario.effect.Reflection;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Никита on 03.12.2016.
 */
public class ApplicationContext {


    private List<Class<?>> classes;
    private Map map;

    public ApplicationContext(String str) throws Exception {
        map = new HashMap<String, Object>();
        this.classes = ClassFinder.getClassesFromPackage(str);
        for (Class clazz: classes) {
            process(clazz);
        }
    }

    public ApplicationContext(Class clazz){

    }

    private void process(Class clazz) throws Exception {
        if(clazz != null){
            Object obj = null;
            if(clazz.isAnnotationPresent(ComponentA.class)) {
                for (Constructor constructor : clazz.getConstructors()) {
                    if (constructor.isAnnotationPresent(Autowired.class)) {

                        Class[] parametersTypes = constructor.getParameterTypes();
                        Object[] parameters = new Object[parametersTypes.length];

                        int i = 0;
                        for (Class c : parametersTypes){
                            parameters[i++] = c.getConstructor().newInstance();
                        }
                        obj = clazz.getConstructor(parametersTypes).newInstance(parameters);


                    } else {
                        obj = clazz.newInstance();
                    }
                }
                for (Method methods : clazz.getMethods()) {
                    if(methods.isAnnotationPresent(Autowired.class)){
                        methods.setAccessible(true);
                        Class[] methodParametersTypes = methods.getParameterTypes();
                        Object[] parameters = new Object[methodParametersTypes.length];

                        int i = 0;
                        for (Class c : methodParametersTypes){
                            parameters[i++] = c.getConstructor().newInstance();
                        }
                        methods.invoke(obj, parameters);
                    }
                }
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        field.setAccessible(true);
                        field.set(obj, field.getType().newInstance());
                    }
                }
                if(obj != null) {
                    String name = ((ComponentA)(clazz.getAnnotation(ComponentA.class))).name();
                    if(name.length() > 0)
                        map.put(name, obj);
                    else
                        map.put(obj.getClass().getName(), obj);
                }
            }
        }
    }


    public Object getBean(String str) throws Exception {
        return map.get(str);
    }
}
