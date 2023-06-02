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
        this.postClass();
        this.postClassOverlapping();
        this.getClassWSearch();
        this.deleteClass();
        this.postBooking();
        this.postBookingAlreadyBooked();
        this.deleteBooking();

    }

    private void getClassWSearch() {
        String expectedGetResponse = "[{\"name\":\"Pilates\",\"date\":\"01-07-2023\",\"capacity\":30,\"bookings\":[]}]";
        ResponseEntity<String> testSearchNameDate = mockClient.exchange("/classes?name=Pilates&date=01-07-2023", HttpMethod.GET, null, String.class);
        assertEquals("Success - GET Search classes after POST request status code", HttpStatus.OK, testSearchNameDate.getStatusCode());
        assertEquals("Success - GET Search classes after POST request response body", expectedGetResponse, testSearchNameDate.getBody());
    }

    private void postClass() {
        String expectedPostResponse = "[{\"name\":\"Pilates\",\"date\":\"01-07-2023\",\"capacity\":30,\"bookings\":[]}," + "{\"name\":\"Pilates\",\"date\":\"02-07-2023\",\"capacity\":30,\"bookings\":[]}," + "{\"name\":\"Pilates\",\"date\":\"03-07-2023\",\"capacity\":30,\"bookings\":[]}," + "{\"name\":\"Pilates\",\"date\":\"04-07-2023\",\"capacity\":30,\"bookings\":[]}," + "{\"name\":\"Pilates\",\"date\":\"05-07-2023\",\"capacity\":30,\"bookings\":[]}]";
        String requestBody = "{\"name\": \"Pilates\", \"startDate\": \"01-07-2023\", \"endDate\": \"05-07-2023\", \"capacity\": 30}";
        ResponseEntity<String> testPostOk = mockClient.exchange("/classes", HttpMethod.POST, genHttpEntityJson(requestBody), String.class);
        assertEquals("Success - POST classes status code", HttpStatus.OK, testPostOk.getStatusCode());
        assertEquals("Success - POST classes response body", expectedPostResponse, testPostOk.getBody());
    }

    private void postClassOverlapping() {
        String requestBody = "{\"name\": \"Zumba\", \"startDate\": \"04-07-2023\", \"endDate\": \"12-07-2023\", \"capacity\": 30}";
        ResponseEntity<String> testPostOverlappingClass = mockClient.exchange("/classes", HttpMethod.POST, genHttpEntityJson(requestBody), String.class);
        assertEquals("Error - POST overlapping class dates", HttpStatus.FORBIDDEN, testPostOverlappingClass.getStatusCode());
    }

    private void deleteClass() {
        String expectedDeleteResponse = "[{\"name\":\"Pilates\",\"date\":\"01-07-2023\",\"capacity\":30,\"bookings\":[]}," + "{\"name\":\"Pilates\",\"date\":\"02-07-2023\",\"capacity\":30,\"bookings\":[]}," + "{\"name\":\"Pilates\",\"date\":\"03-07-2023\",\"capacity\":30,\"bookings\":[]}," + "{\"name\":\"Pilates\",\"date\":\"04-07-2023\",\"capacity\":30,\"bookings\":[]}]";
        String requestBody = "05-07-2023";
        ResponseEntity<String> testDeleteOk = mockClient.exchange("/classes", HttpMethod.DELETE, genHttpEntityJson(requestBody), String.class);
        assertEquals("Success - DELETE a class after POST request status code", HttpStatus.OK, testDeleteOk.getStatusCode());
        assertEquals("Success - DELETE a class after POST request response body", expectedDeleteResponse, testDeleteOk.getBody());
    }

    private void postBooking() {
        String requestBody = "{\"name\": \"John Doe\", \"date\": \"01-07-2023\"}";
        String expectedPostResponse = "[{\"name\":\"Pilates\",\"date\":\"01-07-2023\",\"capacity\":30,\"bookings\":[\"John Doe\"]}," + "{\"name\":\"Pilates\",\"date\":\"02-07-2023\",\"capacity\":30,\"bookings\":[]}," + "{\"name\":\"Pilates\",\"date\":\"03-07-2023\",\"capacity\":30,\"bookings\":[]}," + "{\"name\":\"Pilates\",\"date\":\"04-07-2023\",\"capacity\":30,\"bookings\":[]}]";
        ResponseEntity<String> testPostOk = mockClient.exchange("/bookings", HttpMethod.POST, genHttpEntityJson(requestBody), String.class);
        assertEquals("Success - POST bookings status code", HttpStatus.OK, testPostOk.getStatusCode());
        assertEquals("Success - POST bookings response body", expectedPostResponse, testPostOk.getBody());
    }

    private void deleteBooking() {
        String requestBody = "{\"name\": \"John Doe\", \"date\": \"01-07-2023\"}";
        String expectedDeleteResponse = "[{\"name\":\"Pilates\",\"date\":\"01-07-2023\",\"capacity\":30,\"bookings\":[]}," + "{\"name\":\"Pilates\",\"date\":\"02-07-2023\",\"capacity\":30,\"bookings\":[]}," + "{\"name\":\"Pilates\",\"date\":\"03-07-2023\",\"capacity\":30,\"bookings\":[]}," + "{\"name\":\"Pilates\",\"date\":\"04-07-2023\",\"capacity\":30,\"bookings\":[]}]";
        ResponseEntity<String> testDeleteOk = mockClient.exchange("/bookings", HttpMethod.DELETE, genHttpEntityJson(requestBody), String.class);
        assertEquals("Success - DELETE bookings status code", HttpStatus.OK, testDeleteOk.getStatusCode());
        assertEquals("Success - DELETE bookings response body", expectedDeleteResponse, testDeleteOk.getBody());
    }

    private void postBookingAlreadyBooked(){
        String requestBody = "{\"name\": \"John Doe\", \"date\": \"01-07-2023\"}";
        ResponseEntity<String> testPostErr = mockClient.exchange("/bookings", HttpMethod.POST, genHttpEntityJson(requestBody), String.class);
        assertEquals("Error - POST a booking that already exists", HttpStatus.FORBIDDEN, testPostErr.getStatusCode());
    }


    @Test
    public void getClassesNoParam() {
        ResponseEntity<String> testNoSearchParam = mockClient.exchange("/classes", HttpMethod.GET, null, String.class);
        assertEquals("Success - GET classes without search parameter", HttpStatus.OK, testNoSearchParam.getStatusCode());
    }

    @Test
    public void getClassNameParam() {
        ResponseEntity<String> testSearchName = mockClient.exchange("/classes?name=Pilates", HttpMethod.GET, null, String.class);
        assertEquals("Success - GET classes with name as search parameter", HttpStatus.OK, testSearchName.getStatusCode());
    }

    @Test
    public void getClassDateParam() {
        ResponseEntity<String> testSearchDate = mockClient.exchange("/classes?date=01-07-2023", HttpMethod.GET, null, String.class);
        assertEquals("Success - GET classes with date as search parameter", HttpStatus.OK, testSearchDate.getStatusCode());
    }

    @Test
    public void getClassNameDateParam() {
        ResponseEntity<String> testSearchNameDate = mockClient.exchange("/classes?name=Pilates&date=01-07-2023", HttpMethod.GET, null, String.class);
        assertEquals("Success - GET classes with name and date as search parameters", HttpStatus.OK, testSearchNameDate.getStatusCode());
    }

    @Test
    public void getClassInvalidDateParam() {
        ResponseEntity<String> testDateFormat = mockClient.exchange("/classes?name=TEST&date=TestWrongFormat", HttpMethod.GET, null, String.class);
        assertEquals("Error - GET classes with invalid date as search parameter", HttpStatus.BAD_REQUEST, testDateFormat.getStatusCode());
    }

    @Test
    public void postClassInvalidName() {
        String requestBody = "{\"name\": null, \"startDate\": \"01-10-2023\", \"endDate\": \"10-10-2023\", \"capacity\": 30}";
        ResponseEntity<String> testPostInvalidName = mockClient.exchange("/classes", HttpMethod.POST, genHttpEntityJson(requestBody), String.class);
        assertEquals("Error - POST classes with invalid name", HttpStatus.BAD_REQUEST, testPostInvalidName.getStatusCode());
    }

    @Test
    public void postClassInvalidDateInterval() {
        String requestBody = "{\"name\": \"Zumba\", \"startDate\": \"05-08-2023\", \"endDate\": \"01-08-2023\", \"capacity\": 30}";
        ResponseEntity<String> testPostInvalidDateInterval = mockClient.exchange("/classes", HttpMethod.POST, genHttpEntityJson(requestBody), String.class);
        assertEquals("Error - POST classes with invalid date interval", HttpStatus.BAD_REQUEST, testPostInvalidDateInterval.getStatusCode());
    }

    @Test
    public void postClassInvalidDateFormat() {
        String requestBody = "{\"name\": \"Zumba\", \"startDate\": \"05-09-2023\", \"endDate\": \"invalid\", \"capacity\": 30}";
        ResponseEntity<String> testPostInvalidDateFormat = mockClient.exchange("/classes", HttpMethod.POST, genHttpEntityJson(requestBody), String.class);
        assertEquals("Error - POST classes with invalid date format", HttpStatus.BAD_REQUEST, testPostInvalidDateFormat.getStatusCode());
    }

    @Test
    public void deleteClassNotFound() {
        String requestBody = "05-07-2024";
        ResponseEntity<String> testPostOk = mockClient.exchange("/classes", HttpMethod.DELETE, genHttpEntityJson(requestBody), String.class);
        assertEquals("Success - DELETE a class on a day that there's no booked classes", HttpStatus.NOT_FOUND, testPostOk.getStatusCode());
    }

    @Test
    public void deleteClassInvalidDate() {
        String requestBody = "2023-04-05";
        ResponseEntity<String> testPostOk = mockClient.exchange("/classes", HttpMethod.DELETE, genHttpEntityJson(requestBody), String.class);
        assertEquals("Error - DELETE a class with invalid date format", HttpStatus.BAD_REQUEST, testPostOk.getStatusCode());
    }


    @Test
    public void deleteBookingNotFound(){
        String requestBody = "{\"name\": \"John Doe\", \"date\": \"03-07-2023\"}";
        ResponseEntity<String> testDeleteErr = mockClient.exchange("/bookings", HttpMethod.DELETE, genHttpEntityJson(requestBody), String.class);
        assertEquals("Error - DELETE a booking that doesn't exists", HttpStatus.NOT_FOUND, testDeleteErr.getStatusCode());
    }

    @Test
    public void postBookingInvalidDate() {
        String requestBody = "{\"date\": \"invalid\"}";
        ResponseEntity<String> testPostOk = mockClient.exchange("/bookings", HttpMethod.POST, genHttpEntityJson(requestBody), String.class);
        assertEquals("Error - POST a booking without member name", HttpStatus.BAD_REQUEST, testPostOk.getStatusCode());
    }

    @Test
    public void postBookingMissingName() {
        String requestBody = "{\"date\": \"01-07-2023\"}";
        ResponseEntity<String> testPostOk = mockClient.exchange("/bookings", HttpMethod.POST, genHttpEntityJson(requestBody), String.class);
        assertEquals("Error - POST a booking without member name", HttpStatus.BAD_REQUEST, testPostOk.getStatusCode());
    }

    @Test
    public void postBookingMissingDate() {
        String requestBody = "{\"name\": \"John Doe\"}";
        ResponseEntity<String> testPostOk = mockClient.exchange("/bookings", HttpMethod.POST, genHttpEntityJson(requestBody), String.class);
        assertEquals("Error - POST a booking without date", HttpStatus.BAD_REQUEST, testPostOk.getStatusCode());
    }

    @Test
    public void deleteBookingInvalidDate() {
        String requestBody = "{\"date\": \"invalid\"}";
        ResponseEntity<String> testDeleteOk = mockClient.exchange("/bookings", HttpMethod.DELETE, genHttpEntityJson(requestBody), String.class);
        assertEquals("Error - DELETE a booking without member name", HttpStatus.BAD_REQUEST, testDeleteOk.getStatusCode());
    }

    @Test
    public void deleteBookingMissingName() {
        String requestBody = "{\"date\": \"01-07-2023\"}";
        ResponseEntity<String> testDeleteOk = mockClient.exchange("/bookings", HttpMethod.DELETE, genHttpEntityJson(requestBody), String.class);
        assertEquals("Error - DELETE a booking without member name", HttpStatus.BAD_REQUEST, testDeleteOk.getStatusCode());
    }

    @Test
    public void deleteBookingMissingDate() {
        String requestBody = "{\"name\": \"John Doe\"}";
        ResponseEntity<String> testDeleteOk = mockClient.exchange("/bookings", HttpMethod.DELETE, genHttpEntityJson(requestBody), String.class);
        assertEquals("Error - DELETE a booking without a date", HttpStatus.BAD_REQUEST, testDeleteOk.getStatusCode());
    }


    private HttpEntity<String> genHttpEntityJson(String requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        return requestEntity;
    }
}
