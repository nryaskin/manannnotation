package context;

import annotation.Autowired;
import annotation.ComponentA;
import com.sun.scenario.effect.Reflection;
import com.sun.scenario.effect.light.Light;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;

import java.lang.reflect.*;
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
        for (Class clazz : classes) {
            startProcess(clazz);
        }
    }

    public ApplicationContext(Class clazz) {

    }

    private void startProcess(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        chooseComponents(clazz);
    }


    private void chooseComponents(Class<?> clazz) throws IllegalAccessException, InstantiationException {
        if (clazz != null) {
            if (clazz.isAnnotationPresent(ComponentA.class)) {
                Object object = clazz.newInstance();
                ComponentA componentA = (ComponentA) clazz.getAnnotation(ComponentA.class);
                map.put((componentA.name().length() == 0) ? clazz.getName() : componentA.name(), object);
            }
        }
    }


    private void process(Class clazz) throws Exception {
        /*if(clazz != null){
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
        }*/
    }


    private Object processAutowired(Object object) throws InvocationTargetException, NoSuchMethodException, InstantiationException, NoSuchBeanException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        for (Constructor c :
                clazz.getDeclaredConstructors()) {
            if (c.isAnnotationPresent(Autowired.class)) {
                object = autowireConstructer(object, c);
                break;
            }
        }

        for (Method m :
                clazz.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Autowired.class))
                autowireMethod(object, m);
        }

        for (Field f :
                clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(Autowired.class))
                autowireField(object, f);
        }

        return object;
    }

    private void autowireMethod(Object object, Method m) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, NoSuchBeanException {
        boolean flag = m.isAccessible();
        if (!flag)
            m.setAccessible(true);
        Class[] methodParametersTypes = m.getParameterTypes();
        Object[] parameters = initParameters(methodParametersTypes);
        m.invoke(object, parameters);
        if (!flag)
            m.setAccessible(false);
    }

    private Object autowireConstructer(Object object, Constructor con) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchBeanException {

        Class[] parametersTypes = con.getParameterTypes();
        Object[] parameters = initParameters(parametersTypes);

        object = object.getClass().getDeclaredConstructor(parametersTypes).newInstance(parameters);

        return object;
    }

    private Object[] initParameters(Class[] parametersTypes) throws NoSuchBeanException {
        Object[] parameters = new Object[parametersTypes.length];

        int i = 0;
        for (Class c : parametersTypes) {
            if (c.isAnnotationPresent(ComponentA.class)) {

                ComponentA componentA = (ComponentA) c.getAnnotation(ComponentA.class);
                Object inst = map.get((componentA.name().length() == 0) ? c.getName() : componentA.name());
                if (inst != null)
                    parameters[i++] = inst;
                else
                    throw new NoSuchBeanException();

            } else
                throw new NoSuchBeanException();
        }
        return parameters;
    }

    private void autowireField(Object object, Field f) throws IllegalAccessException, InstantiationException {
        boolean flag = f.isAccessible();
        if (!flag)
            f.setAccessible(true);

        ComponentA componentA = (ComponentA) f.getType().getAnnotation(ComponentA.class);
        Object inst = map.get((componentA.name().length() == 0) ? f.getType().getName() : componentA.name());


        f.set(object, inst);
        if(!flag)
            f.setAccessible(false);
    }

    public Object getBean(String str) throws Exception {

        return processAutowired(map.get(str));

    }
}
