package com.example.ClassesBookings;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClassesBookingsApplicationTests {

    @Autowired
    private TestRestTemplate mockClient;

    @Test
    void contextLoads() {
    }

    @Test
    public void mainTests() {
        this.testClasses();
    }

    private void testClasses() {
        this.testPostClass();
        this.testPostOverlappingClass();
        this.testGetClassWSearch();
        this.testDeleteClass();
        this.testPostBooking();
    }

    private void testPostBooking(){

    }

    private void testPostClass() {
        String expectedPostResponse = "[{\"name\":\"Pilates\",\"date\":\"01-07-2023\",\"capacity\":30,\"bookings\":[]}," +
                "{\"name\":\"Pilates\",\"date\":\"02-07-2023\",\"capacity\":30,\"bookings\":[]}," +
                "{\"name\":\"Pilates\",\"date\":\"03-07-2023\",\"capacity\":30,\"bookings\":[]}," +
                "{\"name\":\"Pilates\",\"date\":\"04-07-2023\",\"capacity\":30,\"bookings\":[]}," +
                "{\"name\":\"Pilates\",\"date\":\"05-07-2023\",\"capacity\":30,\"bookings\":[]}]";

        String requestBody = "{\"name\": \"Pilates\", \"startDate\": \"01-07-2023\", \"endDate\": \"05-07-2023\", \"capacity\": 30}";
        ResponseEntity<String> testPostOk = mockClient.exchange("/classes", HttpMethod.POST, genHttpEntityJson(requestBody), String.class);
        assertEquals("Success - POST classes status code", HttpStatus.OK, testPostOk.getStatusCode());
        assertEquals("Success - POST classes response body", expectedPostResponse, testPostOk.getBody());
    }

    private void testPostOverlappingClass() {
        String requestBody = "{\"name\": \"Zumba\", \"startDate\": \"04-07-2023\", \"endDate\": \"12-07-2023\", \"capacity\": 30}";
        ResponseEntity<String> testPostOverlappingClass = mockClient.exchange("/classes", HttpMethod.POST, genHttpEntityJson(requestBody), String.class);
        assertEquals("Error - POST overlapping class dates", HttpStatus.FORBIDDEN, testPostOverlappingClass.getStatusCode());
    }

    private void testGetClassWSearch() {
        String expectedGetResponse = "[{\"name\":\"Pilates\",\"date\":\"01-07-2023\",\"capacity\":30,\"bookings\":[]}]";
        ResponseEntity<String> testSearchNameDate = mockClient.exchange("/classes?name=Pilates&date=01-07-2023", HttpMethod.GET, null, String.class);
        assertEquals("Success - GET Search classes after POST request status code", HttpStatus.OK, testSearchNameDate.getStatusCode());
        assertEquals("Success - GET Search classes after POST request response body", expectedGetResponse, testSearchNameDate.getBody());
    }

    private void testDeleteClass() {
        String expectedDeleteResponse = "[{\"name\":\"Pilates\",\"date\":\"01-07-2023\",\"capacity\":30,\"bookings\":[]}," +
                "{\"name\":\"Pilates\",\"date\":\"02-07-2023\",\"capacity\":30,\"bookings\":[]}," +
                "{\"name\":\"Pilates\",\"date\":\"03-07-2023\",\"capacity\":30,\"bookings\":[]}," +
                "{\"name\":\"Pilates\",\"date\":\"04-07-2023\",\"capacity\":30,\"bookings\":[]}]";
        String requestBody = "05-07-2023";
        ResponseEntity<String> testPostOk = mockClient.exchange("/classes", HttpMethod.DELETE, genHttpEntityJson(requestBody) , String.class);
        assertEquals("Success - DELETE a class after POST request status code", HttpStatus.OK, testPostOk.getStatusCode());
        assertEquals("Success - DELETE a class after POST request response body", expectedDeleteResponse, testPostOk.getBody());
    }


    @Test
    public void testPostInvalidDateInterval() {
        String requestBody3 = "{\"name\": \"Zumba\", \"startDate\": \"05-08-2023\", \"endDate\": \"01-08-2023\", \"capacity\": 30}";
        ResponseEntity<String> testPostInvalidDateInterval = mockClient.exchange("/classes", HttpMethod.POST, genHttpEntityJson(requestBody3), String.class);
        assertEquals("Error - POST classes with invalid date interval", HttpStatus.BAD_REQUEST, testPostInvalidDateInterval.getStatusCode());
    }

    @Test
    public void testPostInvalidDateFormat() {
        String requestBody4 = "{\"name\": \"Zumba\", \"startDate\": \"05-09-2023\", \"endDate\": \"invalid\", \"capacity\": 30}";
        ResponseEntity<String> testPostInvalidDateFormat = mockClient.exchange("/classes", HttpMethod.POST, genHttpEntityJson(requestBody4), String.class);
        assertEquals("Error - POST classes with invalid date format", HttpStatus.BAD_REQUEST, testPostInvalidDateFormat.getStatusCode());
    }

    @Test
    public void testPostInvalidName() {
        String requestBody5 = "{\"name\": null, \"startDate\": \"01-10-2023\", \"endDate\": \"10-10-2023\", \"capacity\": 30}";
        ResponseEntity<String> testPostInvalidName = mockClient.exchange("/classes", HttpMethod.POST, genHttpEntityJson(requestBody5), String.class);
        assertEquals("Error - POST classes with invalid name", HttpStatus.BAD_REQUEST, testPostInvalidName.getStatusCode());
    }

    @Test
    public void testGetClasses() {

        ResponseEntity<String> testNoSearchParam = mockClient.exchange("/classes", HttpMethod.GET, null, String.class);
        assertEquals("Success - GET classes without search parameter", HttpStatus.OK, testNoSearchParam.getStatusCode());

        ResponseEntity<String> testSearchName = mockClient.exchange("/classes?name=Pilates", HttpMethod.GET, null, String.class);
        assertEquals("Success - GET classes with name as search parameter", HttpStatus.OK, testSearchName.getStatusCode());

        ResponseEntity<String> testSearchDate = mockClient.exchange("/classes?date=01-07-2023", HttpMethod.GET, null, String.class);
        assertEquals("Success - GET classes with date as search parameter", HttpStatus.OK, testSearchDate.getStatusCode());

        ResponseEntity<String> testSearchNameDate = mockClient.exchange("/classes?name=Pilates&date=01-07-2023", HttpMethod.GET, null, String.class);
        assertEquals("Success - GET classes with name and date as search parameters", HttpStatus.OK, testSearchNameDate.getStatusCode());

        ResponseEntity<String> testDateFormat = mockClient.exchange("/classes?name=TEST&date=TestWrongFormat", HttpMethod.GET, null, String.class);
        assertEquals("Error - GET classes with invalid date as search parameter", HttpStatus.BAD_REQUEST, testDateFormat.getStatusCode());
    }

    @Test
    public void testDeleteNotFound() {
        String requestBody = "05-07-2024";
        ResponseEntity<String> testPostOk = mockClient.exchange("/classes", HttpMethod.DELETE, genHttpEntityJson(requestBody), String.class);
        assertEquals("Success - DELETE a class on a day that there's no booked classes", HttpStatus.NOT_FOUND, testPostOk.getStatusCode());
    }

    @Test
    public void testDeleteInvalidDate() {
        String requestBody = "2023-04-05";
        ResponseEntity<String> testPostOk = mockClient.exchange("/classes", HttpMethod.DELETE, genHttpEntityJson(requestBody), String.class);
        assertEquals("Error - DELETE a class with invalid date format", HttpStatus.BAD_REQUEST, testPostOk.getStatusCode());
    }

    @Test
    public void testDeleteNullDate() {
        ResponseEntity<String> testPostOk = mockClient.exchange("/classes", HttpMethod.DELETE, null, String.class);
        assertEquals("Error - DELETE a class with a null date", HttpStatus.BAD_REQUEST, testPostOk.getStatusCode());
    }


    private HttpEntity<String> genHttpEntityJson(String requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        return requestEntity;
    }
}
