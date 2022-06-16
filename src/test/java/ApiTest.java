import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ApiTest {

    private final String COUNTRY_NAME = "YY";
    private final String INCORRECT_ID = "bad";
    private int createdCountryId;
    private int notExistingCountryId;
    private int uniqueCountryId;


    @BeforeAll
    public static void setUpAuth() {
        PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
        authScheme.setUserName("admin");
        authScheme.setPassword("admin");
        RestAssured.authentication = authScheme;
    }

    @BeforeAll
    public static void setUpErrorLogging() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }


    @BeforeEach
    public void createCountryToTest() {
        createdCountryId = given()
                .contentType("application/json")
                .body("{\n" +
                        "  \"countryName\": \"" + COUNTRY_NAME + "\"\n" +
                        "}")
                .when()
                .post("/api/countries")
                .then()
                .extract()
                .path("id");
        notExistingCountryId = createdCountryId + 999;
    }

    @AfterEach
    public void deleteCreatedCountryToTest() {
        delete("/api/countries/" + createdCountryId);
        delete("/api/countries/" + uniqueCountryId);
    }


    @Nested
    @DisplayName("Tests for post method")
    class PostMethodTest {

        @Test
        public void createUniqueCountry() {
            uniqueCountryId = given()
                    .contentType("application/json")
                    .body("{\n" +
                            "  \"countryName\": \"UN\"\n" +
                            "}")
                    .when()
                    .post("/api/countries")
                    .then()
                    .statusCode(201)
                    .body("id", not(empty()))
                    .extract()
                    .path("id");
            when()
                    .get("/api/countries/" + uniqueCountryId)
                    .then()
                    .body("id", is(uniqueCountryId),
                            "countryName", is("UN"),
                            "locations", nullValue());
        }

        @Test
        public void createNotUniqueCountry() {
            given()
                    .contentType("application/json")
                    .body("{\n" +
                            "  \"countryName\": \"ZZ\"\n" +
                            "}")
                    .when()
                    .post("/api/countries");
            given()
                    .contentType("application/json")
                    .body("{\n" +
                            "  \"countryName\": \"ZZ\"\n" +
                            "}")
                    .when()
                    .post("/api/countries")
                    .then()
                    .statusCode(500)
                    .body("title", is("Internal Server Error"),
                            "message", is("error.http.500"));
        }

        @Test
        public void creatingCountryWithNameLengthOverTwoCharacters() {
            given()
                    .contentType("application/json")
                    .body("{\n" +
                            "  \"countryName\": \"tst\"\n" +
                            "}")
                    .when()
                    .post("/api/countries")
                    .then()
                    .statusCode(400)
                    .body("title", is("Method argument not valid"),
                            "message", is("error.validation"),
                            "fieldErrors[0].objectName", is("country"),
                            "fieldErrors[0].field", is("countryName"),
                            "fieldErrors[0].message", is("size must be between 2 and 2"));
        }

        @Test
        public void creatingCountryWithNameLengthLessThanTwoCharacters() {
            given()
                    .contentType("application/json")
                    .body("{\n" +
                            "  \"countryName\": \"t\"\n" +
                            "}")
                    .when()
                    .post("/api/countries")
                    .then()
                    .statusCode(400)
                    .body("title", is("Method argument not valid"),
                            "message", is("error.validation"),
                            "fieldErrors[0].objectName", is("country"),
                            "fieldErrors[0].field", is("countryName"),
                            "fieldErrors[0].message", is("size must be between 2 and 2"));
        }

        @Test
        public void creatingCountryWithoutRequestBody() {
            given()
                    .contentType("application/json")

                    .when()
                    .post("/api/countries")
                    .then()
                    .statusCode(400)
                    .body("title", is("Bad Request"),
                            "message", is("error.http.400"),
                            "detail", containsString("Required request body is missing"));
        }

        @Test
        public void creatingCountryWithNullName() {
            given()
                    .contentType("application/json")
                    .body("{\n" +
                            " \"countryName\":" + null +
                            "}")
                    .when()
                    .post("/api/countries")
                    .then()
                    .statusCode(400)
                    .body("title", is("Method argument not valid"),
                            "message", is("error.validation"),
                            "fieldErrors[0].objectName", is("country"),
                            "fieldErrors[0].field", is("countryName"),
                            "fieldErrors[0].message", is("must not be null"));
        }
    }

    @Nested
    @DisplayName("Tests for get method")
    class GetMethodTest {

        @Test
        public void getCountryById() {
            when()
                    .get("/api/countries/" + createdCountryId)
                    .then()
                    .statusCode(200)
                    .body("id", not(empty()),
                            "countryName", is(COUNTRY_NAME),
                            "locations", nullValue());
        }

        @Test
        public void getCountryByNotExistingId() {
            when()
                    .get("/api/countries/" + notExistingCountryId)
                    .then()
                    .statusCode(404)
                    .body("title", is("Not Found"));
        }

        @Test
        public void getCountryByIncorrectId() {
            when()
                    .get("/api/countries/" + INCORRECT_ID)
                    .then()
                    .statusCode(400)
                    .body("title", is("Bad Request"));
        }

    }

    @Nested
    @DisplayName("Tests for patch method")
    class PatchMethodTest {

        @Test
        public void patchingCountry() {
            given()
                    .contentType("application/json")
                    .body("{\n" +
                            "  \"id\": \"" + createdCountryId + "\",\n" +
                            "  \"countryName\": \"PA\"\n" +
                            "}")
                    .when()
                    .patch("/api/countries/" + createdCountryId)
                    .then()
                    .statusCode(200)
                    .body("id", is(createdCountryId),
                            "countryName", is("PA"),
                            "locations", nullValue());
            when()
                    .get("/api/countries/" + createdCountryId)
                    .then()
                    .statusCode(200)
                    .body("id", is(createdCountryId),
                            "countryName", is("PA"),
                            "locations", nullValue());
        }

        @Test
        public void patchingCountryWithNotExistingId() {
            given()
                    .contentType("application/json")
                    .body("{\n" +
                            "  \"id\": \"" + notExistingCountryId + "\",\n" +
                            "  \"countryName\": \"PA\"\n" +
                            "}")
                    .when()
                    .patch("/api/countries/" + notExistingCountryId)
                    .then()
                    .statusCode(400)
                    .body("title", is("Entity not found"),
                            "entityName", is("country"),
                            "errorKey", is("idnotfound"));
        }

        @Test
        public void patchingCountryWithNotEqualsIdsInBodyAndUrl() {
            given()
                    .contentType("application/json")
                    .body("{\n" +
                            "  \"id\": \"" + notExistingCountryId + "\",\n" +
                            "  \"countryName\": \"PA\"\n" +
                            "}")
                    .when()
                    .patch("/api/countries/" + createdCountryId)
                    .then()
                    .statusCode(400)
                    .body("title", is("Invalid ID"),
                            "entityName", is("country"),
                            "errorKey", is("idinvalid"));
        }

        @Test
        public void patchingCountryWithIncorrectId() {
            given()
                    .contentType("application/json")
                    .body("{\n" +
                            "  \"id\": \"" + INCORRECT_ID + "\",\n" +
                            "  \"countryName\": \"PA\"\n" +
                            "}")
                    .when()
                    .patch("/api/countries/" + INCORRECT_ID)
                    .then()
                    .statusCode(400)
                    .body("title", is("Bad Request"));
        }

        @Test
        public void patchingCountryWithIncorrectCountryName() {
            given()
                    .contentType("application/json")
                    .body("{\n" +
                            "  \"id\": \"" + createdCountryId + "\",\n" +
                            "  \"countryName\": \"PAT\"\n" +
                            "}")
                    .when()
                    .patch("/api/countries/" + createdCountryId)
                    .then()
                    .statusCode(500)
                    .body("detail", containsString("Could not commit JPA transaction"));
        }

        @Test
        public void patchingCountryWithAlreadyExistingCountryName() {
            given()
                    .contentType("application/json")
                    .body("{\n" +
                            "  \"countryName\": \"AE\"\n" +
                            "}")
                    .when()
                    .post("/api/countries");
            given()
                    .contentType("application/json")
                    .body("{\n" +
                            "  \"id\": \"" + createdCountryId + "\",\n" +
                            "  \"countryName\": \"AE\"\n" +
                            "}")
                    .when()
                    .patch("/api/countries/" + createdCountryId)
                    .then()
                    .statusCode(500)
                    .body("detail", containsString("could not execute batch"));
        }

    }

    @Nested
    @DisplayName("Tests for delete method")
    class DeleteMethodTest {

        @Test
        public void deleteCountry() {
            when()
                    .delete("/api/countries/" + createdCountryId)
                    .then()
                    .statusCode(204);
            when()
                    .get("/api/countries/" + createdCountryId)
                    .then()
                    .statusCode(404)
                    .body("title", is("Not Found"));
        }

        @Test
        public void deleteCountryWithNotExistingId() {
            when()
                    .delete("/api/countries/" + notExistingCountryId)
                    .then()
                    .statusCode(500)
                    .body("detail", is("No class com.tinkoff.edu.domain.Country entity with id " +
                            notExistingCountryId + " exists!"));
        }

        @Test
        public void deleteCountryWithIncorrectId() {
            when()
                    .delete("/api/countries/" + INCORRECT_ID)
                    .then()
                    .statusCode(400)
                    .body("title", is("Bad Request"));
        }

    }

}

