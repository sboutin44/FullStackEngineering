import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.seb.email.routing.EmailServiceProvider;
import com.seb.email.routing.MyEmail;
import junit.framework.TestCase;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.core.JsonParser;

import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EmailServiceProviderTest extends TestCase {

    private String JSON_FILE_DIRECTORY = "src/test/json/";
    private String JSON_FILE_TESTEMAIL = "testEmail.json";
    private MyEmail email;
    private EmailServiceProvider provider;

    protected void setUp() throws Exception {
        super.setUp();

        // Get a test json from JSON_FILE_DIRECTORY
        Path pathToJson = Paths.get(JSON_FILE_DIRECTORY + JSON_FILE_TESTEMAIL);
        String jsonString = new String(Files.readAllBytes(pathToJson), StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(jsonString);
        email = new MyEmail();

        // html parsing
        Document docBody = Jsoup.parse(jsonObject.getString("body")); // Convert the html as a plaintext Document object
        String emailBody = docBody.text();

        email.setTo(jsonObject.getString("to"));
        email.setToName(jsonObject.getString("to_name"));
        email.setFrom(jsonObject.getString("from"));
        email.setFromName(jsonObject.getString("from_name"));
        email.setSubject(jsonObject.getString("subject"));
        email.setBody(emailBody);

        // To identify the email in my mailbox
        email.setSubject("Tests");
    }

    protected void tearDown() throws Exception {
        super.tearDown();

    }

    public void testSendMessageViaElasticEmail() throws UnsupportedEncodingException {

        provider = new EmailServiceProvider(EmailServiceProvider.Providers.ELASTICEMAIL);
        provider.setUrl(provider.URL_ELASTICEMAIL);
        assertEquals(HttpStatus.OK, provider.SendMessageViaElasticEmail(email));
    }

    public void testSendMessageViaMAILGUN() {

        provider = new EmailServiceProvider(EmailServiceProvider.Providers.MAILGUN);
        provider.setUrl(provider.URL_MAILGUN);
        assertEquals(HttpStatus.OK, provider.SendMessageViaMAILGUN(email));

    }


/*
    public void testSendMessageViaElasticEmail_wrongStatus() throws UnsupportedEncodingException {

        provider = new EmailServiceProvider(EmailServiceProvider.Providers.ELASTICEMAIL);
        assertEquals(HttpStatus.OK, provider.SendMessageViaElasticEmail(email));
    }

    public void testSendMessageViaMAILGUN_wrongStatus() {

        //provider.setUrl(local_fake_url);
        provider = new EmailServiceProvider(EmailServiceProvider.Providers.MAILGUN);
        assertEquals(HttpStatus.OK, provider.SendMessageViaMAILGUN(email));

    }
*/

}
