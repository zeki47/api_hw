package api_hw;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;
public class hw2 {

    @BeforeClass
    public void beforeclass() {
        baseURI = "http://54.161.136.45:8000";
    }
    /*
    Q1:
Given accept type is json
And path param id is 20
When user sends a get request to "/api/spartans/{id}"
Then status code is 200
And content-type is "application/json;charset=UTF-8"
And response header contains Date
And Transfer-Encoding is chunked
And response payload values match the following:
id is 20,
name is "Lothario",
gender is "Male",
phone is 7551551687
     */

    @Test
    public void getOneSpartan_path(){
        Response response = given().accept(ContentType.JSON)
                .and().pathParam("id", 20)
                .when().get("/api/spartans/{id}");

        Assert.assertEquals(response.statusCode(),200);
        Assert.assertEquals(response.contentType(),"application/json;charset=UTF-8");

        Assert.assertTrue(response.headers().hasHeaderWithName("Date"));
        Assert.assertEquals(response.header("Transfer-Encoding"),"chunked");

        int id=response.path("id");
        String name=response.path("name");
        String gender=response.path("gender");
        long phone=response.path("phone");

        Assert.assertEquals(id,20);
        Assert.assertEquals(name,"Lothario");
        Assert.assertEquals(gender,"Male");
        Assert.assertEquals(phone,7551551687l);


    }
    /*
        Q2:
        Given accept type is json
        And query param gender = Female
        And queary param nameContains = r
        When user sends a get request to "/api/spartans/search"
        Then status code is 200
        And content-type is "application/json;charset=UTF-8"
        And all genders are Female
        And all names contains r
        And size is 20
        And totalPages is 1
        And sorted is false
     */
    @Test
    public void SpartanTestWithQueryParam(){
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("gender","Female");
        queryMap.put("nameContains","r");

        Response response=given().accept(ContentType.JSON)
                .and().queryParams(queryMap)
                .when().get("/api/spartans/search");

        JsonPath jsonPath=response.jsonPath();

        Assert.assertEquals(response.statusCode(),200);
        Assert.assertEquals(response.contentType(),"application/json;charset=UTF-8");
        //get all country ids
        List<String> genders = jsonPath.getList("content.gender");


        for (String gender : genders) {
            System.out.println("gender = " + gender);
            assertEquals(gender,"Female");
        }

        List<String> names = jsonPath.getList("content.name");

        for(String name : names){
            System.out.println("name = " + name);
            assertTrue(name.toLowerCase().contains("r"));
        }

        int size=jsonPath.getInt("size");
        System.out.println(size);
        int totalPages=jsonPath.getInt("totalPages");
        System.out.println(totalPages);
        boolean sorted=jsonPath.getBoolean("sort.sorted");
        System.out.println(sorted);
        Assert.assertEquals(size,20);
        Assert.assertEquals(totalPages,1);
        Assert.assertEquals(sorted,false);












    }

}
