package exception;

/**
 * Created by wshwbluebird on 2017/4/11.
 */
public class WrongValueException extends Exception {
    String err;
    public WrongValueException(String err){
            this.err = err;
    }


    public String  getErr(){
        return err;
    }
}
