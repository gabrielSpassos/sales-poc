Feature: Create Sale

  Background:
    * url salesServiceBaseUrl
    * def salesEndpoint = '/v1/sales'
    * header Accept = 'application/json'
    * header Content-Type = 'application/json'

  @CreateSale
  Scenario: Submit vote to assembly
    * def personRequestBody = read('resources/personRequest.json')
    Given path salesEndpoint
    And request personRequestBody
    When method POST
    Then status 200
    And match response ==
    """
    {
      "id": #notnull,
      "status": "LEAD",
      "registerDateTime": #notnull,
      "person": {
        "nationalIdentificationNumber": "80344455092",
        "birthdate": "1995-03-12",
        "firstName": "Juan",
        "lastName": "Moura",
        "email": "juan.moura@gmail.com"
      }
    }
    """

  Scenario: Get sale by id
    * def createSaleScenario = callonce read('classpath:com/gabrielspassos/poc/sales/sales.feature@CreateSale')
    * def saleId = createSaleScenario.response.id
    Given path salesEndpoint, saleId
    When method GET
    Then status 200
    And match response ==
    """
    {
      "id": #notnull,
      "status": #notnull,
      "registerDateTime": #notnull,
      "person": {
        "nationalIdentificationNumber": "80344455092",
        "birthdate": "1995-03-12",
        "firstName": "Juan",
        "lastName": "Moura",
        "email": "juan.moura@gmail.com"
      }
    }
    """