package my.finr.finr.exceptionHandling;

import org.springframework.http.HttpStatus;

public record ResponseError(HttpStatus http_status, Integer code, String message) {}
