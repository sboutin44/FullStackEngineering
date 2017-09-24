import com.seb.email.routing.MyEmail;
import junit.framework.TestCase;
import org.json.JSONObject;

import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EmailServiceProviderTest extends TestCase {

    private String JSON_FILE_DIRECTORY = "src/test/json/";
    private String JSON_FILE_TESTEMAIL = "testEmail.json";

    protected void setUp () throws Exception {
        super.setUp();

        // Get a test json from JSON_FILE_DIRECTORY
        Path pathToJson = Paths.get(JSON_FILE_DIRECTORY + JSON_FILE_TESTEMAIL);
        String jsonString = new String(Files.readAllBytes(pathToJson), StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(jsonString);
        //MyEmail email1 = new MyEmail();
    }

    public void SendMessageViaElasticEmail (){

    }

    public void testSendMessageViaMAILGUN (){
        System.out.println("test");
    }

}
