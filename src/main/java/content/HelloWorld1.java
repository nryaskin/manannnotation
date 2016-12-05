package content;

import annotation.Autowired;
import annotation.ComponentA;

/**
 * Created by Никита on 05.12.2016.
 */
@ComponentA
public class HelloWorld1 {
    PrintService printService;

    @Autowired
    public HelloWorld1(PrintService printSrevice){
        this.printService = printSrevice;
    }

    public void printMessage(String message){
        printService.printMessage(message);
    }

}
