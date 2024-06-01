package com.api.test;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class ApiTest {

    private static final String BASE_URL = "https://juice-shop.herokuapp.com";
    private String token;
    private String basketId;

    @BeforeMethod
    public void signIn() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = given();

        request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
        request.body("{ \"email\": \"ahsansayel@gmail.com\", \"Ahsan@1234\": \"your_password\" }");

        Response response = request.post("/rest/user/login");
        response.then().statusCode(200);

        token = response.jsonPath().getString("authentication.token");
        basketId = response.jsonPath().getString("authentication.bid");
    }











}
