package fr.diginamic.bibliothequeWeb.controllers.advice;

import fr.diginamic.bibliothequeWeb.exceptions.ClientError;
import fr.diginamic.bibliothequeWeb.exceptions.EmpruntError;
import fr.diginamic.bibliothequeWeb.exceptions.LivreError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.*;
import java.sql.SQLException;

@RestControllerAdvice
public class ErrorController {

    public ErrorController() {
        // TODO Auto-generated constructor stub
    }

    @ExceptionHandler(value = {ClientError.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String errorClientException(ClientError e) {

        return "Soucis sur le controlleur Client : " + e.getMessage();
    }

    @ExceptionHandler(value = {LivreError.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String errorClientException(LivreError e) {

        return "Soucis sur le controlleur Livre : " + e.getMessage();
    }

    @ExceptionHandler(value = {EmpruntError.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String errorClientException(EmpruntError e) {

        return "Soucis sur le controlleur Emprunt : " + e.getMessage();
    }

    @ExceptionHandler(value =
            {ConstraintDeclarationException.class, ConstraintDefinitionException.class,
                    UnexpectedTypeException.class, ConstraintViolationException.class,
                    SQLException.class, ValidationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)

    public String errorSqlException(Exception e) {

        return "Erreur d'intégrité ou de validation : " + e.getMessage();
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String errorGeneralException(Exception e) {

        return "Il y a une erreur : " + e.getMessage();
    }


}
