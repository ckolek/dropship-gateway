package me.kolek.ecommerce.dsgw.api.controller.v1.auth;

import me.kolek.ecommerce.dsgw.auth.AuthException;
import me.kolek.ecommerce.dsgw.exception.AlreadyExistsException;
import me.kolek.ecommerce.dsgw.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
  @ExceptionHandler(AuthException.class)
  public ResponseEntity<String> handleAuthException(AuthException exception) {
    return (switch (exception.getType()) {
      case UNAUTHORIZED -> ResponseEntity.status(HttpStatus.UNAUTHORIZED);
      case FORBIDDEN -> ResponseEntity.status(HttpStatus.FORBIDDEN);
    }).body(exception.getMessage());
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handleNotFoundException(NotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(AlreadyExistsException.class)
  public ResponseEntity<String> handleAlreadyExistsException(AlreadyExistsException exception) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
  }
}
