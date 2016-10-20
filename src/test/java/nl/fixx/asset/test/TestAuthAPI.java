package nl.fixx.asset.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestAuthAPI {
    
    @Test
    public void authAPITest(){
	String response = CallRestAuthAPI.doCall();
	assertNotNull(response);
    }

}
