package com.portifolio.apibff.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.portifolio.apibff.exceptions.web.ApiError;
import com.portifolio.apibff.exceptions.web.ResponseEntityBuilder;
import feign.FeignException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        List<String> details = new ArrayList();


        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

        details.add(builder.toString());

        var err = new ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Invalid JSON", details);

        return ResponseEntityBuilder.build(err);

    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<String> details = new ArrayList();
        details.add(ex.getMessage());

        var err = new ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Malformed JSON request", details);

        return ResponseEntityBuilder.build(err);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<String> details = new ArrayList();
        details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getObjectName() + " : " + error.getDefaultMessage())
                .collect(Collectors.toList());

        var err = new ApiError(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                "Validation Errors",
                details);

        return ResponseEntityBuilder.build(err);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        List<String> details = new ArrayList();
        details.add(ex.getParameterName() + " parameter is missing");

        var err = new ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Missing Parameters", details);

        return ResponseEntityBuilder.build(err);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
        List<String> details = new ArrayList();
        details.add(ex.getMessage());

        var err = new ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Mismatch Type", details);

        return ResponseEntityBuilder.build(err);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(Exception ex, WebRequest request) {

        List<String> details = new ArrayList();
        details.add(ex.getMessage());

        var err = new ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Constraint Violation", details);

        return ResponseEntityBuilder.build(err);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {

        List<String> details = new ArrayList();
        details.add(ex.getMessage());

        var err = new ApiError(LocalDateTime.now(), HttpStatus.NOT_FOUND, "Resource Not Found", details);

        return ResponseEntityBuilder.build(err);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<String> details = new ArrayList();
        details.add(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));

        var err = new ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Method Not Found", details);

        return ResponseEntityBuilder.build(err);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {

        List<String> details = new ArrayList();
        details.add(ex.getLocalizedMessage());

        var err = new ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Error occurred", details);

        return ResponseEntityBuilder.build(err);

    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignException(FeignException e, HttpServletResponse response) throws JsonProcessingException {
        var err = new ApiError();
        List<String> details = new ArrayList();
        details.add(e.getMessage());

        switch (e.status()) {
            case 400: {
                err = new ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST, "Malformed JSON request", details);
                break;
            }
            case 404: {
                ObjectMapper jsonMapper = new ObjectMapper();
                jsonMapper.registerModule(new JavaTimeModule());
                err = jsonMapper.readValue(e.contentUTF8(), ApiError.class);
                break;
            }
            case 503: {
                err = new ApiError(LocalDateTime.now(), HttpStatus.SERVICE_UNAVAILABLE, "Service Unavailable", details);
                break;
            }
            case 500: {
                err = new ApiError(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", details);
                break;
            }
        }

        return ResponseEntityBuilder.build(err);
    }
}