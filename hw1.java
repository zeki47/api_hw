package api_hw;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;
public class hw1 {
    @BeforeClass
    public void beforeclass() {
        baseURI = "http://54.161.136.45:1000/ords";
    }
    /*
    ORDS API:
    Q1:
            - Given accept type is Json
- Path param value- US
- When users sends request to /countries
- Then status code is 200
            - And Content - Type is Json
- And country_id is US
- And Country_name is United States of America
- And Region_id is 2
   */

    @Test
    public void test1(){
        Response response=given().accept(ContentType.JSON)
                .pathParam("id","US").
                        when().get("/hr/countries/{id}");
        JsonPath jsonPath=response.jsonPath();
        String countryId= jsonPath.getString("country_id");
        String countryName= jsonPath.getString("country_name");
        int region_id=jsonPath.getInt("region_id");
        Assert.assertEquals(response.statusCode(),200);
        Assert.assertEquals(response.contentType(),"application/json");
        Assert.assertEquals(countryId,"US");
        Assert.assertEquals(countryName,"United States of America");

    }

    /*Q2:
- Given accept type is Json
- Query param value - q={"department_id":80}
- When users sends request to /employees
- Then status code is 200
- And Content - Type is Json
- And all job_ids start with 'SA'
- And all department_ids are 80
- Count is 25
*/

    @Test
    public void test2(){

        Response response=given().accept(ContentType.JSON).
                queryParam("q","{\"department_id\":80}").
                when().get("/hr/employees");

        JsonPath jsonPath=response.jsonPath();

        int count=jsonPath.getInt("count");
        System.out.println(count);
        List<Integer> departmentIds = jsonPath.getList("items.department_id");
        System.out.println(departmentIds);
        List<String> jobIds = jsonPath.getList("items.job_id");
        System.out.println(jobIds);

        Assert.assertEquals(response.statusCode(),200);
        Assert.assertEquals(response.contentType(),"application/json");

        for(String jobId:jobIds){
            Assert.assertTrue(jobId.startsWith("SA"));
        }

        for(int departmentId:departmentIds){
            Assert.assertEquals(departmentId,80);
        }

        Assert.assertEquals(count,25);


    }

    /*
    Q3:
- Given accept type is Json
-Query param value q= region_id 3
- When users sends request to /countries
- Then status code is 200
- And all regions_id is 3
- And count is 6
- And hasMore is false
- And Country_name are;
Australia,China,India,Japan,Malaysia,Singapore
     */

    @Test
    public void test3(){
        Response response=given().accept(ContentType.JSON).
                queryParam("q","{\"region_id\":3}").
                when().get("/hr/countries");
        JsonPath jsonPath=response.jsonPath();

        List<String> countryNames = jsonPath.getList("items.country_name");
        System.out.println(countryNames);
        List<Integer> regionIds = jsonPath.getList("items.region_id");
        System.out.println(regionIds);

        int count=jsonPath.getInt("count");
        System.out.println(count);
        boolean hasMore=jsonPath.getBoolean("hasMore");
        System.out.println(hasMore);

        List<String>countries= Arrays.asList("Australia","China","India","Japan","Malaysia","Singapore");

        Assert.assertEquals(response.statusCode(),200);
        for(int regionId:regionIds){
            Assert.assertEquals(regionId,3);
        }
        int i=0;
        for(String countryName:countryNames){
            Assert.assertEquals(countryName,countries.get(i));
            i++;
        }

        Assert.assertEquals(count,6);
        Assert.assertEquals(hasMore,false);
    }

}
