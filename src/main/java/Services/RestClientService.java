package Services;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matcher;
import java.util.Collections;
import java.util.Map;
import static io.restassured.RestAssured.given;


public class RestClientService {

    @Step("Делает запрос с телом и параметрами: метод - {httpMethod}, URL - {handle}, тело - {requestBody}, параметры - {queryParams}")
    public Response sendRequestWithQueryParams(Method httpMethod, String handle, Object requestBody, Map<String, Object> queryParams) {
        return given()
                .contentType("application/json")
                .body(requestBody != null ? requestBody : new Object()) // в случае если тело не нужно
                .queryParams(queryParams != null ? queryParams : Collections.emptyMap()) // в случае если параметры не нужны
                .when()
                .request(httpMethod, handle);
    }


    @Step("Делает запрос только с параметрами: метод - {httpMethod}, URL - {handle}, параметры - {queryParams}")
    public Response sendRequestWithQueryParams(Method httpMethod, String handle, Map<String, Object> queryParams) {
        return sendRequestWithQueryParams(httpMethod, handle, null, queryParams);

    }


    @Step("Делает запрос с телом: метод - {httpMethod}, URL - {handle}, тело - {requestBody}")
    public Response sendRequest(Method httpMethod, String handle, Object requestBody) {
        return sendRequestWithQueryParams(httpMethod, handle, requestBody, null);


    }

    @Step("Делает запрос без тела: метод - {httpMethod}, URL - {handle}")
    public Response sendRequest(Method httpMethod, String handle) {
        return sendRequest(httpMethod, handle, null); // Здесь передаем null
    }

    @Step("Проверяет тело и код ответа")
    public ValidatableResponse validateResponse(Response response, int expectedStatusCode) {
        return validateResponse(response, expectedStatusCode, null, null);
    }

    @Step("Проверяет тело и код ответа")
    public ValidatableResponse validateResponse(Response response, String bodyCheck, Matcher matcher) {
        return validateResponse(response, -1, bodyCheck, matcher);
    }

    @Step("Проверяет тело и код ответа")
    public ValidatableResponse validateResponse(Response response, int expectedStatusCode, String bodyCheck, Matcher matcher) {
        ValidatableResponse responseValidatable;
        try {
            responseValidatable = response
                    .then().log()
                    .ifValidationFails();

            if (expectedStatusCode != -1) {
                responseValidatable.statusCode(expectedStatusCode);
            }

            if (bodyCheck != null && matcher != null) {
                responseValidatable.body(bodyCheck, matcher);
            }
        } catch (AssertionError error) {
            logValidationError(error, response);
            throw error;
        }

        return responseValidatable;
    }

    @Step("Логгирование ошибок")
    public void logValidationError(Throwable error,Response response) {
        Allure.addAttachment("Ошибка", error.getMessage());
        Allure.addAttachment("Тело ответа", response.getBody().asString());
    }
}
