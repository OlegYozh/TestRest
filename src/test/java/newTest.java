import groovy.json.StringEscapeUtils;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class newTest {

    @Test
    public void test0() {
        String companyName = StringEscapeUtils.escapeJava("Альбатрос").toLowerCase(Locale.ROOT);
        String companyType = StringEscapeUtils.escapeJava("ООО").toLowerCase(Locale.ROOT);
        String emailOwner = "aa+1@mail.com";
        String users = "[\"test_cu_11@mail.com\",\"test_dev@mail.com\",\"ivan@noibiz.com\"]";
        String reqBody = "{\n" +
                "\"company_name\": \"" + companyName + "\",\n" +
                "\"company_users\": " + users + ",\n " +
                "\"email_owner\": \"" + emailOwner + "\",\n" +
                "\"company_type\": \"" + companyType + "\"\n" +
                "} ";
        String respBodyActual = given().when().body(reqBody).get("http://users.bugred.ru/tasks/rest/createcompany").then().assertThat().statusCode(HttpStatus.SC_OK).extract().asString();

        Assert.assertTrue(respBodyActual.contains("\"type\":\"success\""));
        Assert.assertTrue(respBodyActual.contains("\"name\":\"" + companyName + "\""));
        Assert.assertTrue(respBodyActual.contains("\"type\":\"" + companyType + "\""));
        Assert.assertTrue(respBodyActual.contains("\"users\":" + users));
    }

    @Test
    public void test1() {

        String reqBody = "<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wrap=\"http://foo.bar/wrappersoapserver\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">\n" +
                "    <soapenv:Header/>\n" +
                "    <soapenv:Body>\n" +
                "        <wrap:CreateCompany>\n" +
                "            <company_name>123</company_name>\n" +
                "            <company_type>ООО</company_type>\n" +
                "            <email_owner>aa+1@mail.com</email_owner>\n" +
                "            <company_users soapenc:arrayType=\"xsd:array[]\">\n" +
                "                <item>test_cu_11@mail.com</item>\n" +
                "                <item>test_dev@mail.com</item>\n" +
                "                <item>ivan@noibiz.com</item>\n" +
                "            </company_users>\n" +
                "        </wrap:CreateCompany>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        String respBodyActual = given().when().body(reqBody).post("http://users.bugred.ru/tasks/soap/WrapperSoapServer.php").then().assertThat().statusCode(HttpStatus.SC_OK).extract().asPrettyString();
        System.out.println(respBodyActual);

        Assert.assertTrue(respBodyActual.contains("<message xsi:type=\"xsd:string\">Компания успешно создана!</message>"));
    }
}
