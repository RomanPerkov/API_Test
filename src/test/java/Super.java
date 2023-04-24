import Services.RestClientService;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.Matchers.equalTo;

public class Super {


    protected static Faker faker = new Faker();
    protected static Map<String, Object> fake = generateRequestBody();
    protected RestClientService restClientService = new RestClientService();

    protected static final String URL = "https://qa-scooter.praktikum-services.ru";
    protected static final String CREATE_COURIER_ENDPOINT = "/api/v1/courier";
    protected static final String LOGIN_COURIER_ENDPOINT = "/api/v1/courier/login";
    protected static final String DELETE_COURIER_ENDPOINT = "/api/v1/courier/";


    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
    }

    @Before
    @Step("Создает курьера")
    public void createCourier() {
        // Создание курьера
        Response response = restClientService.sendRequest(Method.POST, CREATE_COURIER_ENDPOINT, fake);
        restClientService.validateResponse(response, 201, "ok", equalTo(true));
    }

    @Step("Делает логин и вовзращает id курьера")
    public int loginResponse() {
        Response loginResponse = restClientService.sendRequest(Method.POST, LOGIN_COURIER_ENDPOINT, fake);
        restClientService.validateResponse(loginResponse, 200);
        return loginResponse.path("id");
    }

    @Step("Создаем юзера")
    public static Map<String, Object> generateRequestBody() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("login", faker.name().username());
        requestBody.put("password", faker.internet().password());
        return requestBody;

    }

    @After
    @Step("Удаляет курьера")
    public void deleteCourier() {
      // Удаление созданного курьера
        Response deleteResponse = restClientService.sendRequest(Method.DELETE, DELETE_COURIER_ENDPOINT + loginResponse());
        deleteResponse.then().log().body().statusCode(200).body("ok", equalTo(true));
    }
}
