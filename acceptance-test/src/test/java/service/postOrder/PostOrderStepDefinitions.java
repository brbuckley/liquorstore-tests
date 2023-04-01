package service.postOrder;

import static java.util.concurrent.TimeUnit.SECONDS;
import static net.serenitybdd.rest.SerenityRest.rest;
import static net.serenitybdd.rest.SerenityRest.then;
import static org.awaitility.Awaitility.await;

import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.hamcrest.Matchers;

public class PostOrderStepDefinitions {

  private String api;
  private String productId, customerId, orderId;
  private int quantity;
  private static String token;

  @BeforeAll
  public static void auth() {
    EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();
    token = variables.getProperty("token");
  }

  @Given("I want to create a new Order")
  public void iWantToCreateANewOrder() {
    api = "http://localhost:8080/{customerId}/orders";
  }

  @And("The new Order should be {int} from product {word} for customer {word}")
  public void theNewOrderShouldBeFromProductForCustomer(
      int quantity, String productId, String customerId) {
    this.quantity = quantity;
    this.productId = productId;
    this.customerId = customerId;
  }

  @And("The new Order should be product {word} for customer {word}")
  public void theNewOrderShouldBeForCustomer(String productId, String customerId) {
    this.productId = productId;
    this.customerId = customerId;
  }

  @Then("The system gives me an order with a new id and status {word}")
  public void theSystemGivesMeAnOrderWithANewIdAndStatus(String status) {
    orderId = then().body("id", Matchers.matchesRegex("ORD[0-9]{7}")).extract().path("id");
    then().body("status", Matchers.equalTo(status));
  }

  @Then("The system shows: {string}")
  public void theSystemShows(String status) {
    then().body("description", Matchers.equalTo(status));
  }

  @When("The incomplete order is received by the system")
  public void theIncompleteOrderIsReceivedByTheSystem() {
    rest()
        .header("Content-Type", "application/json")
        .auth()
        .oauth2(token)
        .body(
            "{\n"
                + "  \"items\": [\n"
                + "    {\n"
                + "      \"product\": {\n"
                + "        \"id\": \""
                + productId
                + "\"\n"
                + "      }\n"
                + "    }\n"
                + "  ]\n"
                + "}")
        .post(api, customerId);
  }

  @When("The order is received by the system")
  public void theOrderIsReceivedByTheSystem() {
    rest()
        .header("Content-Type", "application/json")
        .auth()
        .oauth2(token)
        .body(
            "{\n"
                + "  \"items\": [\n"
                + "    {\n"
                + "      \"quantity\": "
                + quantity
                + ",\n"
                + "      \"product\": {\n"
                + "        \"id\": \""
                + productId
                + "\"\n"
                + "      }\n"
                + "    }\n"
                + "  ]\n"
                + "}")
        .post(api, customerId);
  }

  @And("After a short time, the order status changes to {word}")
  public void afterAShortTimeTheOrderStatusChangesTo(String status) {
    await()
        .timeout(60, SECONDS)
        .until(
            () -> {
              rest()
                  .header("Content-Type", "application/json")
                  .auth()
                  .oauth2(token)
                  .get("/" + customerId + "/orders/" + orderId);
              return then().extract().path("status").equals(status);
            });
  }
}
