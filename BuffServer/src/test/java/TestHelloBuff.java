import org.junit.Test;

import java.rmi.RemoteException;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

/**
 * Created by wshwbluebird on 2017/3/2.
 */
public class TestHelloBuff {
    @Test
	public void test1() {
        HelloBuff helloBuff = new HelloBuff();
        assertEquals("Hello World", helloBuff.runProject());

	}
}
