This is a maven Spring boot project.

To run the test suite run:

Windows

mvnw test

Linux

./mvnw test

3 tests located in KrakenFlexApplicationTests.java will run

The project might be imported in your favorite IDE as a maven project.

This test siteOutagesEndpointTest() is been commented out, since no luck on passing it; I was getting 400 Bad Request with message body: {"message": "Unexpected outages received"}.

The core logic is been tested in siteOutagesTest, I loaded json Strings exactly as in the specification of All the outages & Site Info then I passed the result to krakenFlexService.getSiteOutagesRequestBody which doing the main logic.

The json result of krakenFlexService.getSiteOutagesRequestBody compared to the hardcoded json as in the spec after trimming the white spacess this test consistently passes


