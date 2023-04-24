import io.qameta.allure.Description;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.Matchers.equalTo;


public class CreateCourierTest extends Super {


    private static Integer duplicateCourierId = null;


    @Description("Тест проверяет попытку создания дупликата курьера")
    @Test
    public void createDuplicateCourierTest() {
        Response response = restClientService.sendRequest(Method.POST, CREATE_COURIER_ENDPOINT, fake);
        if(response.getStatusCode()==201){
            duplicateCourierId = response.path("id");
        }
        restClientService.validateResponse(response, 409, "message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Description("Тест проверяет попытку создание курьера без пароля")
    @Test
    public void createCourierNoPasswordTest() {
        Map<String,Object> missLogin = new HashMap<>(generateRequestBody());
        missLogin.remove("login");
        Response response = restClientService.sendRequest(Method.POST, CREATE_COURIER_ENDPOINT, missLogin);
        restClientService.validateResponse(response, 400, "message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Description("Тест проверяет попытку создание курьера без логина")
    @Test
    public void createCourierNoLoginTest(){
        Map<String,Object> missPass = new HashMap<>(generateRequestBody());
        missPass.remove("password");
        Response response = restClientService.sendRequest(Method.POST, CREATE_COURIER_ENDPOINT, missPass);
        restClientService.validateResponse(response, 400, "message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Description("Метод удаляет дупликат курьера если таковой появится")
    @After
    public void deleteDuplicateCourier(){
        if (duplicateCourierId != null) {
            Response deleteResponse = restClientService.sendRequest(Method.DELETE, DELETE_COURIER_ENDPOINT + duplicateCourierId);
            restClientService.validateResponse(deleteResponse, 200, "ok", equalTo(true));
            duplicateCourierId = null;
        }
    }

}
