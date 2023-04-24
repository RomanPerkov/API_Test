package POJO;


import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Courier {

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("metroStation")
    private String metroStation;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("rentTime")
    private String rentTime;

    @JsonProperty("deliveryDate")
    private String deliveryDate;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("color")
    private List<String> color;

}
