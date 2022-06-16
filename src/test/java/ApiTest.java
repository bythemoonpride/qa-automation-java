import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ApiTest {

    private static Connection connection;
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

    @BeforeAll
    public static void connect() throws SQLException {
        connection = DriverManager.getConnection(

                "jdbc:postgresql://localhost/app-db",
                "app-db-admin",
                "P@ssw0rd"
        );
    }

    @BeforeEach
    public void createCountryInDb() throws SQLException {
        int biggestId = -1;
        Statement sqlSelect = connection.createStatement();
        ResultSet resultSet = sqlSelect.executeQuery("SELECT * FROM country order by id desc limit 1");
        if (resultSet.next()) {
            biggestId = resultSet.getInt(1);
        }
        createdCountryId = biggestId + 1;
        notExistingCountryId = createdCountryId + 999;

        PreparedStatement sqlInsert = connection.prepareStatement(
                "INSERT INTO country(id, country_name) VALUES(?,?)"
        );
        sqlInsert.setInt(1, createdCountryId);
        sqlInsert.setString(2, COUNTRY_NAME);
        sqlInsert.executeUpdate();
    }


    @AfterEach
    public void deleteCreatedCountryToTest() throws SQLException {
        PreparedStatement sqlDeleteCreatedCountry = connection.prepareStatement(
                "delete from country where id =?"
        );
        sqlDeleteCreatedCountry.setInt(1, createdCountryId);
        sqlDeleteCreatedCountry.executeUpdate();

        PreparedStatement sqlDeleteUniqueCountry = connection.prepareStatement(
                "delete from country where id =?"
        );
        sqlDeleteUniqueCountry.setInt(1, uniqueCountryId);
        sqlDeleteUniqueCountry.executeUpdate();
    }

    @AfterAll
    public static void disconnect() throws SQLException {
        connection.close();
    }


    @Nested
    @DisplayName("Tests for post method")
    class PostMethodTest {

        @Test
        public void createUniqueCountry() throws SQLException {
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


            Collection<Integer> countryIds = new ArrayList<>();

            Statement sql = connection.createStatement();
            ResultSet resultSet = sql.executeQuery("SELECT * FROM country WHERE country_name = 'UN'");
            while (resultSet.next()) {
                countryIds.add(resultSet.getInt(1));
            }
            assertThat(countryIds.size(), is(1));
            assertThat(countryIds, hasItem(uniqueCountryId));
        }

        @Test
        public void createNotUniqueCountry() throws SQLException {
            PreparedStatement sqlUpdate = connection.prepareStatement(
                    "UPDATE public.country SET country_name = 'ZZ' WHERE id = ?"
            );
            sqlUpdate.setInt(1, createdCountryId);
            sqlUpdate.executeUpdate();

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
        @Disabled
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
        public void patchingCountry() throws SQLException {
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

            Collection<Integer> countryIds = new ArrayList<>();

            Statement sql = connection.createStatement();
            ResultSet resultSet = sql.executeQuery("SELECT * FROM country WHERE country_name = 'PA'");
            while (resultSet.next()) {
                countryIds.add(resultSet.getInt(1));
            }
            assertThat(countryIds.size(), is(1));
            assertThat(countryIds, hasItem(createdCountryId));
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
                    .statusCode(500);
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
                    .statusCode(500);

        }

    }

    @Nested
    @DisplayName("Tests for delete method")
    class DeleteMethodTest {

        @Test
        public void deleteCountry() throws SQLException {
            when()
                    .delete("/api/countries/" + createdCountryId)
                    .then()
                    .statusCode(204);

            Collection<Integer> countryIds = new ArrayList<>();

            Statement sql = connection.createStatement();
            ResultSet resultSet = sql.executeQuery("SELECT * FROM country WHERE id = '" + createdCountryId + "'");
            while (resultSet.next()) {
                countryIds.add(resultSet.getInt(1));
            }
            assertThat(countryIds.size(), is(0));
        }

        @Test
        public void deleteCountryWithNotExistingId() {
            when()
                    .delete("/api/countries/" + notExistingCountryId)
                    .then()
                    .statusCode(500);
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


    @Test @Disabled
    //Вариант работы с инсертом в бд записи о стране, когда id записи автогенерируемый
    public void insertCountryWhenIdIsAutogenerated() throws SQLException {
        PreparedStatement sql = connection.prepareStatement(
                "INSERT INTO country(country_name) VALUES(?)",
                Statement.RETURN_GENERATED_KEYS
        );
        sql.setString(1, "ZZ");
        sql.executeUpdate();

        List<Integer> countryIds = new ArrayList<>();
        ResultSet keys = sql.getGeneratedKeys();
        while (keys.next()) {
            countryIds.add(keys.getInt(1));
        }
        createdCountryId = countryIds.get(0);
    }

}

