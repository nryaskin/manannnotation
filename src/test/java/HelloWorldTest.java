import content.HelloWorld;
import content.HelloWorld1;
import content.HelloWorldSetter;
import context.ApplicationContext;
import org.junit.Test;

/**
 * Created by Никита on 03.12.2016.
 */

public class HelloWorldTest {
    @Test
    public void printingMessageHelloWorldUsingPrivateFieldInjection() throws Exception {
        ApplicationContext ctx = new ApplicationContext("content");

        HelloWorld helloWorld = (HelloWorld)ctx.getBean("helloWorld");
        helloWorld.printMessage("Hello, World!");
    }
    @Test
    public void printingMessageHelloworld1UsingConstructorInjection() throws Exception{
        ApplicationContext ctx = new ApplicationContext("content");

        HelloWorld1 helloWorld1 = (HelloWorld1)ctx.getBean(HelloWorld1.class.getName());
        helloWorld1.printMessage("Hello, World!");
    }
    @Test
    public void printingMessageHelloworld1UsingSetterInjection() throws Exception{
        ApplicationContext ctx = new ApplicationContext("content");

        HelloWorldSetter helloWorld1 = (HelloWorldSetter)ctx.getBean("helloWorldSetter");
        helloWorld1.printMessage("Hello, World!");
    }

}
