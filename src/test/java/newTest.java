import groovy.json.StringEscapeUtils;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.junit.matchers.JUnitMatchers.containsString;

public class newTest {
    private static final String restPath = "http://users.bugred.ru/tasks/rest/createcompany";
    private static final String soapPath = "http://users.bugred.ru/tasks/soap/WrapperSoapServer.php";

    private static String companyName = "Тестовый";
    private static String companyType = "ООО";
    private static String companyNameASCII = StringEscapeUtils.escapeJava("Тестовый").toLowerCase(Locale.ROOT);
    private static String companyTypeASCII = StringEscapeUtils.escapeJava("ООО").toLowerCase(Locale.ROOT);
    private static String emailOwner = "aa+1@mail.com";
    private static String users = "[\"test_cu_11@mail.com\",\"test_dev@mail.com\",\"ivan@noibiz.com\"]";
    private static String usersXML =
            "                <item>test_cu_11@mail.com</item>\n" +
                    "                <item>test_dev@mail.com</item>\n" +
                    "                <item>ivan@noibiz.com</item>\n";
    private static ResponseSpecification responseSpec0 = new ResponseSpecBuilder()
            .expectStatusCode(HttpStatus.SC_OK)
            .expectBody(containsString("\"type\":\"success\""))
            .expectBody(containsString("\"name\":\"" + companyNameASCII + "\""))
            .expectBody(containsString("\"type\":\"" + companyTypeASCII + "\""))
            .expectBody(containsString("\"users\":" + users))
            .build();
    private static ResponseSpecification responseSpec1 = new ResponseSpecBuilder()
            .expectStatusCode(HttpStatus.SC_OK)
            .expectBody(containsString("<message xsi:type=\"xsd:string\">Компания успешно создана!</message>"))
            .build();
    private static final String restReq = "{\n" +
            "\"company_name\": \"" + companyNameASCII + "\",\n" +
            "\"company_users\": " + users + ",\n " +
            "\"email_owner\": \"" + emailOwner + "\",\n" +
            "\"company_type\": \"" + companyTypeASCII + "\"\n" +
            "} ";
    private static final String soapReq =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wrap=\"http://foo.bar/wrappersoapserver\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">\n" +
                    "    <soapenv:Header/>\n" +
                    "    <soapenv:Body>\n" +
                    "        <wrap:CreateCompany>\n" +
                    "            <company_name>" + companyName + "</company_name>\n" +
                    "            <company_type>" + companyType + "</company_type>\n" +
                    "            <email_owner>" + emailOwner + "</email_owner>\n" +
                    "            <company_users soapenc:arrayType=\"xsd:array[]\">\n" + usersXML +
                    "            </company_users>\n" +
                    "        </wrap:CreateCompany>\n" +
                    "    </soapenv:Body>\n" +
                    "</soapenv:Envelope>";

    @Test
    public void test0() {

        given().when().body(restReq)
                .post(restPath)
                .then()
                .spec(responseSpec0);
    }

    @Test
    public void test1() {
        given().when().body(soapReq)
                .get(soapPath)
                .then()
                .spec(responseSpec1);

    }
}
