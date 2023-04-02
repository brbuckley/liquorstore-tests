package app.StepDefninitions;

import static java.util.concurrent.TimeUnit.SECONDS;
import static net.serenitybdd.rest.SerenityRest.rest;
import static net.serenitybdd.rest.SerenityRest.then;
import static org.awaitility.Awaitility.await;

import app.model.Order;
import app.model.OrderLine;
import app.model.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.hamcrest.Matchers;

public class OrderStepDefinitions {

  private String api, customerId, orderId;
  Order order;
  ObjectMapper objectMapper =
      new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
  private static String orderToken, purchaseToken;

  @BeforeAll
  public static void auth() {
    EnvironmentVariables variables = SystemEnvironmentVariables.createEnvironmentVariables();
    orderToken = variables.getProperty("token.order");
    purchaseToken = variables.getProperty("token.purchase");
  }

  @Given("I want to create a new Order")
  public void iWantToCreateANewOrder() {
    api = "http://localhost:8080/{customerId}/orders";
    order = new Order();
  }

  @And("The new Order should be {int} from product {word} for customer {word}")
  public void theNewOrderShouldBeFromProductForCustomer(
      int quantity, String productId, String customerId) {
    OrderLine orderLine = new OrderLine(quantity, new Product(productId));
    order.getItems().add(orderLine);
    this.customerId = customerId;
  }

  @And("The new Order should be product {word} for customer {word}")
  public void theNewOrderShouldBeForCustomer(String productId, String customerId) {
    OrderLine orderLine = new OrderLine(null, new Product(productId));
    order.getItems().add(orderLine);
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

  @When("The order is received by the system")
  public void theOrderIsReceivedByTheSystem() throws JsonProcessingException {
    rest()
        .header("Content-Type", "application/json")
        .auth()
        .oauth2(orderToken)
        .body(objectMapper.writeValueAsString(order))
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
                  .oauth2(orderToken)
                  .get("/" + customerId + "/orders/" + orderId);
              return then().extract().path("status").equals(status);
            });
  }

  @And("{int} from product {word}")
  public void fromProduct(int quantity, String productId) {
    OrderLine orderLine = new OrderLine(quantity, new Product(productId));
    order.getItems().add(orderLine);
  }

  @And("{int} new Purchase orders are created")
  public void newPurchaseOrdersAreCreated(int quantity) {
    await()
        .timeout(60, SECONDS)
        .until(
            () -> {
              rest()
                  .header("Content-Type", "application/json")
                  .auth()
                  .oauth2(purchaseToken)
                  .get("http://localhost:8082/?order-id={orderId}", orderId);
              return ((ArrayList<?>) then().extract().path("length")).size() == quantity;
            });
  }

  @And("I do not have an Authorization token for customer {word}")
  public void iDoNotHaveAnAuthorizationTokenForCustomer(String customerId) {
    this.customerId = customerId;
  }

  @When("The order is received by the system with no JWT")
  public void theOrderIsReceivedByTheSystemWithNoJWT() throws JsonProcessingException {
    rest()
        .header("Content-Type", "application/json")
        .body(objectMapper.writeValueAsString(order))
        .post(api, customerId);
  }

  @Then("The system blocks the request with status {int} {word}")
  public void theSystemBlocksTheRequestWithStatus(int status, String description) {
    then().statusCode(status);
  }
}
