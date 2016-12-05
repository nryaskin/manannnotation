package content;

import annotation.Autowired;
import annotation.ComponentA;

/**
 * Created by Никита on 03.12.2016.
 */
@ComponentA(name = "helloWorld")
public class HelloWorld {

    @Autowired
    private PrintService printService;

    public void printMessage(String message){
        printService.printMessage(message);
    }


}
