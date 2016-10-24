package nl.fixx.asset.data;

import static org.junit.Assert.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestAuthAPI {

    @Test
    public void authAPITestPositive() throws ParseException {
        String username = "fixxit";
        String password = "test";

        JSONParser jsonParser = new JSONParser();
        String response = CallRestAuthAPI.doCall(username, password);

        Object obj = jsonParser.parse(response);
        JSONObject jsonObject = (JSONObject) obj;

        String accessToken = jsonObject.get("access_token").toString();

        assertNotNull(accessToken);
    }

    @Test
    public void authAPITestNegative() throws ParseException {
        String username = "courage";
        String password = "dogge";

        String response = CallRestAuthAPI.doCall(username, password);

        assertNull(response);
    }

}
