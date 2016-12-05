package content;

import annotation.ComponentA;

/**
 * Created by Никита on 03.12.2016.
 */
@ComponentA
public class PrintService {

    String message;

    public void setMessage(String message){
        this.message = message;
    }

    public void printMessage(String message){
        System.out.println(message);
    }
}
