package content;

/**
 * Created by Никита on 03.12.2016.
 */
public class PrintService {

    String message;

    public void setMessage(String message){
        this.message = message;
    }

    public void printMessage(){
        System.out.println(message);
    }
}
