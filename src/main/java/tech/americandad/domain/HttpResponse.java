package tech.americandad.domain;

import org.springframework.http.HttpStatus;

public class HttpResponse {

    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String descReason;
    private String message;

    public HttpResponse() {}
    
    
    public HttpResponse(int httpStatusCode, HttpStatus httpStatus, String descReason, String message) {
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
        this.descReason = descReason;
        this.message = message;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
    public String getDescReason() {
        return descReason;
    }
    public void setDescReason(String descReason) {
        this.descReason = descReason;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    
    
}
