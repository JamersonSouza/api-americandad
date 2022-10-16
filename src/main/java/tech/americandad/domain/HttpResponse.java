package tech.americandad.domain;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class HttpResponse {

    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss", timezone = "America/Recife")
    private Date timeStamp;
    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String descReason;
    private String message;
    
    
    public HttpResponse(int httpStatusCode, HttpStatus httpStatus, String descReason, String message) {
        this.timeStamp = new Date();
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


    public Date getTimeStamp() {
        return timeStamp;
    }


    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    
    
}
