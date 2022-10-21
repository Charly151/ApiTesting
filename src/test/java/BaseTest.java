import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.requestSpecification;

public abstract class BaseTest {

    private static final Logger logger = LogManager.getLogger(RestAssured.class);

    @BeforeAll
    public static void setup() throws FileNotFoundException{
        logger.info("Iniciando el configuración");
        requestSpecification = defaultRequestSpecification();
        logger.info("La configuración ha ido bien");
    }

    private static RequestSpecification defaultRequestSpecification() throws FileNotFoundException {
        List<Filter> filters = new ArrayList<>();
        filters.add(new RequestLoggingFilter());
        filters.add(new ResponseLoggingFilter());

        return new RequestSpecBuilder()
                .setBaseUri(ConfVariables.getHost())
                .setBasePath(ConfVariables.getPath())
                .addFilters(filters)
                .setContentType(ContentType.JSON).build();
    }

    private RequestSpecification prodRequestSpecification(){
        return new RequestSpecBuilder()
                .setBaseUri(ConfVariables.getHost())
                .setBasePath(ConfVariables.getPath())
                .setContentType(ContentType.JSON).build();
    }

    public ResponseSpecification defaultResponseSpecification(){
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_OK)
                .expectContentType(ContentType.JSON)
                .build();
    }
}
