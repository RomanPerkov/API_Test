import io.qameta.allure.Description;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;


public class GetOrderListTest extends Super{

    protected static final String ORDERS_ENDPOINT = "/api/v1/orders";

    @Override
    @Before
    public void createCourier() {
    }

    @Override
    @After
    public void deleteCourier() {
    }

    @Description("Тест проверяет получение списка заказов курьера")
    @Test
    public void getOrdersWithCourierIdTest() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("courierId", loginResponse());  // используем loginResponse для получения созданного для тестов courierId
        Response response = restClientService.sendRequestWithQueryParams(Method.GET, ORDERS_ENDPOINT,queryParams);
        restClientService.validateResponse(response, 200,"orders",hasSize(greaterThanOrEqualTo(1)));//проверяем размер orders что в нем есть 1 или больше записей

    }

    @Description("Тест проверяет получение списка заказов несуществующего курьера")
    @Test
    public void getOrderListFakeCourierTest() {
        String noneId = "9999999";
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("courierId", noneId);
        Response response = restClientService.sendRequestWithQueryParams(Method.GET, ORDERS_ENDPOINT, queryParams);
        restClientService.validateResponse(response, 404,"message",equalTo("Курьер с идентификатором " + noneId + " не найден"));

    }

    @Description("Тест проверяет получение списка заказов без CourierId")
    @Test
    public void getOrdersNoIdTest() {
        Response response = restClientService.sendRequest(Method.GET, ORDERS_ENDPOINT);
        restClientService.validateResponse(response, 200, "orders", hasSize(greaterThanOrEqualTo(1))); // проверяем размер orders, что в нем есть 1 или больше записей
    }
}



