import io.restassured.http.*;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;

import static  io.restassured.path.json.JsonPath.from;

public class RestAssuredClass extends BaseTest{

    @Test
    public void LoginRequestTest(){
        //comentado 1
                given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}")
                .post("login")
                .then()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test
    public void getSingleUserTest(){

                given()
                .contentType(ContentType.JSON)
                .get("/users/2")
                .then()
                .statusCode(200)
                .body("data.email", equalTo("janet.weaver@reqres.in"));
    }

    @Test
    public void deleteUserTest(){
        given()
                .delete("users/2")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void patchUserTest(){
        String nombreActualizado =
        given()
                .when()
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}")
                .patch("users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath().getString("name");
        assertThat(nombreActualizado, equalTo("morpheus"));
    }

    @Test
    public void putUserTest(){
        String jobUpdated =
                given()
                        .when()
                        .body("{\n" +
                                "    \"name\": \"morpheus\",\n" +
                                "    \"job\": \"zion resident\"\n" +
                                "}")
                        .put("users/2")
                        .then()
                        .statusCode(HttpStatus.SC_OK)
                        .extract()
                        .jsonPath().getString("job");
        assertThat(jobUpdated, equalTo("zion resident"));
    }

    @Test
    public void getAllUsersTest(){
        Response response = given().get("users?page=2");
        Headers headers = response.getHeaders();
        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();
        String contentType = response.getContentType();

        System.out.println(headers);
        System.out.println(statusCode);
        System.out.println(body);
        System.out.println(contentType);

        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
    }

    @Test
    public void getUsersTest(){
        String response = given().when().get("users?page=2").then().extract().body().asString();

        int page = from(response).get("page");
        int totalPages = from(response).get("total_pages");
        int idFirstUser = from(response).get("data[0].id");
        System.out.println("page " + page);
        System.out.println("total_page " + totalPages);
        System.out.println("Id_First_User " + idFirstUser);

        List<Map> idsMayoresa10 = from(response).get("data.findAll{ user -> user.id > 10}");

        List<Map> usuarios = from(response).get("data.findAll{ user -> user.id > 10 && user.last_name == 'Howell'}");

        //String email = idsMayoresa10.get(0).get("email").toString();
        String email = usuarios.get(0).get("email").toString();

        System.out.println("Email del primer usuario de id mayor a 10 y apellido Howell " + email);
    }

    @Test
    public void getNumUsersTest(){
        String response = given().when().get("users?page=2").then().extract().body().asString();
        List<Map> ids = from(response).get("data");
        int longitud = ids.size();
        System.out.println("Usuarios: " + longitud);
        assertThat(longitud, equalTo(6));
    }

    @Test
    public void getNumUsersTest2(){ // Otra forma
        String response = given().when().get("users?page=2").then().extract().body().asString();
        int longi = from(response).get("data.size");
        System.out.println("Usuarios: " + longi);
        assertThat(longi, equalTo(6));
    }

    @Test
    public void createUserTest(){
        CreateRequest user = new CreateRequest();
        user.setName("morpheus");
        user.setJob("leader");

        CreateResponse response = given()
                .when()
                .body(user)
                .post("users")
                .then()
                .extract()
                .body()
                .as(CreateResponse.class);

        assertThat(response.getJob(), equalTo("leader"));
    }

    @Test
    public void registerMailTest(){
        RegisterRequest regis = new RegisterRequest();
        regis.setEmail("eve.holt@reqres.in");
        regis.setPassword("pistol");

        RegisterResponse response = given()
                .when()
                .body(regis)
                .post("register")
                .then()
                .extract()
                .body()
                .as(RegisterResponse.class);

        assertThat(response.getId(), equalTo(4));
    }
}
