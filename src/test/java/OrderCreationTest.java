import POJO.Courier;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static org.hamcrest.CoreMatchers.notNullValue;


@RunWith(Parameterized.class)
public class OrderCreationTest extends Super {


    private static final String CREATE_ORDER_ENDPOINT = "/api/v1/orders";

    @Override
    @Before
    public void createCourier() {
    }

    @Override
    @After
    public void deleteCourier() {
    }

    private String color;
    private String expectedResult;

    public OrderCreationTest(String color, String expectedResult) {
        this.color = color;
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {null, "201"},
                {"BLACK", "201"},
                {"GREY", "201"},
                {"BLACK,GREY", "201"}
        });
    }

    @Step("Создаем POJO для теста ")
    public Courier createHuman() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate futureDate = LocalDate.now().plusDays(faker.number().numberBetween(1, 10));
        String formattedDate = futureDate.format(dateFormatter);
        Courier courier = new Courier(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.address().fullAddress(),
                String.valueOf(faker.number().numberBetween(1, 20)),
                faker.phoneNumber().phoneNumber(),
                String.valueOf(faker.number().numberBetween(1, 10)),
                formattedDate,
                faker.lorem().sentence(),
                color != null ? Arrays.asList(color.split(",")) : null
        );
        return courier;
    }


    @Description("Тест проверяет создание заказа с цветом , цветами и без цвета.")
    @Test
    public void CreateOrderTest() {
        Response response = restClientService.sendRequest(Method.POST, CREATE_ORDER_ENDPOINT, createHuman());
        restClientService.validateResponse(response, Integer.parseInt(expectedResult), "track", notNullValue());
    }

}