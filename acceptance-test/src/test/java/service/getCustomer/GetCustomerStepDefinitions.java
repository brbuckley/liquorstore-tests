package service.getCustomer;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static net.serenitybdd.rest.SerenityRest.rest;
import static net.serenitybdd.rest.SerenityRest.then;

import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.hamcrest.Matchers;

public class GetCustomerStepDefinitions {

  private String api;
  private String customerId;
  private static String token;

  @BeforeAll
  public static void auth() {
    EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();
    token = variables.getProperty("token");
  }

  @Given("I want to get a Customer")
  public void iWantToGetACustomer() {
    api = "http://localhost:8080/{customerId}";
  }

  @And("I ask the system for the customer {word}")
  public void iAskTheSystemForTheCustomer(String customerId) {
    this.customerId = customerId;
  }

  @Then("The system gives me a customer with id: {word}")
  public void theSystemGivesMeACustomerWithId(String customerId) {
    then().body("id", Matchers.equalTo(customerId));
    then().assertThat().body(matchesJsonSchemaInClasspath("json/CustomerResponseSchema.json"));
  }

  @Then("The system shows: {string}")
  public void theSystemShows(String status) {
    then().body("description", Matchers.equalTo(status));
  }

  @When("I call the system")
  public void iCallTheSystem() {
    rest().header("Content-Type", "application/json").auth().oauth2(token).get(api, customerId);
  }
}
