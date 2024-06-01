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


    // Sign in to the user account

    @BeforeMethod
    public void signIn() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = given();

        request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
        request.body("{ \"email\": \"ahsansayel@gmail.com\", \"password\": \"Ahsan@1234\" }");

        Response response = request.post("/rest/user/login");
        response.then().statusCode(200);

        token = response.jsonPath().getString("authentication.token");
        basketId = response.jsonPath().getString("authentication.bid");
    }



    // Adding 1 item and verifying only 1 item is in the basket

    @Test
    public void testAddOneItemToBasket() {
        addItemToBasket(1, 1, basketId);
        getBasketContents(1);
    }

    // Adding 2 item then deleting 1 item and verifying only 1 item remain in the basket

    @Test
    public void testAddTwoItemsDeleteOne() {
        addItemToBasket(2, 1, basketId);
        addItemToBasket(3, 1, basketId);
        deleteItemFromBasket(2);
        getBasketContents(1);
    }


    // Method to adding products to basket
    private void addItemToBasket(Integer productId, Integer quantity, String basketId) {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = given();

        request.header("Authorization", "Bearer " + token);
        request.header("Content-Type", "application/json");
        request.header("Accept", "application/json");
        request.body("{ \"ProductId\": " + productId + ", \"quantity\": " + quantity + ", \"BasketId\": \"" + basketId + "\" }");

        Response response = request.post("/api/BasketItems");
        response.then().statusCode(200);
    }


    // Method to get the product count of basket
    private void getBasketContents(int expectedItemCount) {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = given();

        request.header("Authorization", "Bearer " + token);
        request.header("Accept", "application/json");

        Response response = request.get("/rest/basket/" + basketId);
        response.then().statusCode(200);
        response.then().body("data.size()", equalTo(expectedItemCount));
    }


    // Method to delete products from basket
    private void deleteItemFromBasket(Integer productId) {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = given();

        request.header("Authorization", "Bearer " + token);
        request.header("Accept", "application/json");

        Response response = request.delete("/api/BasketItems/" + productId);
        response.then().statusCode(200);
    }
}

