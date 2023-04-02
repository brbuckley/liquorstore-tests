@Smoke
Feature: Create new Order at MS

  Scenario: Unauthorized request
    Given I want to create a new Order
    And I do not have an Authorization token for customer CST0000002
    When The order is received by the system with no JWT
    Then The system blocks the request with status 401 Unauthorized

  Scenario: Request to create a new Order should process status asynchronously
    Given I want to create a new Order
    And The new Order should be 1 from product PRD0000001 for customer CST0000001
    When The order is received by the system
    Then The system gives me an order with a new id and status processing
    And After a short time, the order status changes to ordered

  Scenario: Request to create a new Order should create many Purchase orders
    Given I want to create a new Order
    And The new Order should be 1 from product PRD0000001 for customer CST0000001
    And 3 from product PRD0000003
    When The order is received by the system
    Then The system gives me an order with a new id and status processing
    And 2 new Purchase orders are created

  Scenario: Request to create a new Order for a product that does not exist
    Given I want to create a new Order
    And The new Order should be 3 from product PRD0000005 for customer CST0000001
    When The order is received by the system
    Then The system shows: "Resource Not Found"


  Scenario: Request to create a new Order missing quantity
    Given I want to create a new Order
    And The new Order should be product PRD0000001 for customer CST0000001
    When The order is received by the system
    Then The system shows: "Missing required request parameters"

  Scenario: Request to create a new Order with invalid quantity
    Given I want to create a new Order
    And The new Order should be 0 from product PRD0000001 for customer CST0000001
    When The order is received by the system
    Then The system shows: "Missing required request parameters"

  Scenario: Request to create a new Order for invalid Customer
    Given I want to create a new Order
    And The new Order should be 1 from product PRD0000001 for customer invalid
    When The order is received by the system
    Then The system shows: "Missing required request parameters"