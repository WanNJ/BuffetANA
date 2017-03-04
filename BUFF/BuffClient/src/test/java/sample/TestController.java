package sample;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by wshwbluebird on 2017/3/2.
 */
public class TestController {
    @Test
	public void test1() {
        Controller controller = new Controller();
        assertEquals("HelloFX",controller.runFx());

	}
}
