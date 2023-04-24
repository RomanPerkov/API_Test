import io.qameta.allure.Description;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.Test;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.Matchers.equalTo;


public class CourierLoginTest extends Super {



    @Description("Тест проверяет залогиневание")
    @Test
    public void loginTest() {
        Response response = restClientService.sendRequest(Method.POST, LOGIN_COURIER_ENDPOINT, fake);
        restClientService.validateResponse(response, 200);
    }

    @Description("Тест проверяет попытку авторизации без пароля")
    @Test
    public void loginNoPasswordTest() {

        Object onlyLogin = fake.get("login");
        Map<String, Object> onlyLoginMap = Collections.singletonMap("login", onlyLogin);
        Response response = restClientService.sendRequest(Method.POST, LOGIN_COURIER_ENDPOINT, onlyLoginMap);
        restClientService.validateResponse(response, 400, "message", equalTo("Недостаточно данных для входа"));
    }

    @Description("Тест проверяет попытку авторизации без логина")
    @Test
    public void loginNoLoginTest() {

        Object onlyPassword = fake.get("password");
        Map<String, Object> onlyLoginMap = Collections.singletonMap("password", onlyPassword);
        Response response = restClientService.sendRequest(Method.POST, LOGIN_COURIER_ENDPOINT, onlyLoginMap);
        restClientService.validateResponse(response, 400, "message", equalTo("Недостаточно данных для входа"));
    }

    @Description("Тест проверяет попытку авторизации с неправильным паролем")
    @Test
    public void wrongPasswordTest() {
        Map<String, Object> wrongPassword = new HashMap<>(fake);
        wrongPassword.put("password", "NDkc9Lc3MK3xmCj");
        Response response = restClientService.sendRequest(Method.POST, LOGIN_COURIER_ENDPOINT, wrongPassword);
        restClientService.validateResponse(response, 404, "message", equalTo("Учетная запись не найдена"));
    }


    @Description("Тест проверяет попытку авторизации с неправильным логином")
    @Test
    public void wrongLoginTest() {
        Map<String, Object> wrongLogin = new HashMap<>(fake);
        wrongLogin.put("login", "snvj");
        Response response = restClientService.sendRequest(Method.POST, LOGIN_COURIER_ENDPOINT, wrongLogin);
        restClientService.validateResponse(response, 404, "message", equalTo("Учетная запись не найдена"));
    }

}


