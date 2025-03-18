import java.awt.event.*;
import java.lang.reflect.*;

/**
 * @author Cay Horstmann
 * @version 1.00 2004-08-17
 * Modified by Yao Zhao 2023-04-18
 */
public class ActionListenerInstaller {

    /**
     * Processes all ActionListenerFor annotations in the given object.
     *
     * @param obj an object whose methods may have ActionListenerFor annotations
     */
    public static void processAnnotations(Object obj) {
        try {
            Class<?> cl = obj.getClass(); // ButtonFrame
            // For each method in ButtonFrame that are annotated with @ActionListenerFor,
            // add this method as a listener to the corresponding source button
            for (Method m : cl.getDeclaredMethods()) {
                // @ActionListenerFor has RetentionPolicy.RUNTIME,
                // therefore we could access this annotation through reflection
                ActionListenerFor a = m.getAnnotation(ActionListenerFor.class);
                if (a != null) {
                    Field f = cl.getDeclaredField(a.source());
                    f.setAccessible(true);
                    // add the method as an action listener to the button
                    // (specified by "source" in @ActionListenerFor)
                    addListener(f.get(obj), obj, m);
                }
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds an action listener that calls a given method.
     *
     * @param source the event source to which an action listener is added
     * @param param  the implicit parameter of the method that the listener calls
     * @param m      the method that the listener calls
     */
    public static void addListener(Object source, final Object param, final Method m)
            throws ReflectiveOperationException {

        // (event) source is the button
        // this means "button.addActionListener(listener)"
        Method adder = source.getClass().getMethod("addActionListener", ActionListener.class);
        adder.invoke(source, (ActionListener) e -> {
            try {
                m.invoke(param);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
