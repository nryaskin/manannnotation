package content;

import annotation.Autowired;
import annotation.ComponentA;

/**
 * Created by Никита on 05.12.2016.
 */
@ComponentA(name="helloWorldSetter")
public class HelloWorldSetter {
    private PrintService printService;

    @Autowired
    public void setPrintService(PrintService printService){
        this.printService = printService;
    }

    public void printMessage(String message){
        printService.printMessage(message);
    }

}
