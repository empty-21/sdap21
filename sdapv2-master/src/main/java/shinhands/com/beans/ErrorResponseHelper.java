package shinhands.com.beans;

import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import shinhands.com.model.message.Error;
import shinhands.com.model.message.ErrorResponse;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * Error Response helper bean
 *
 */
@ApplicationScoped
@Named("errorResponseHelper")
@RegisterForReflection // Lets Quarkus register this class for reflection during the native build
@Slf4j
public class ErrorResponseHelper {

    public ErrorResponse generateErrorResponse(String id, String description, String message, Object body) {
        ArrayList<String> messages = new ArrayList<String>(0);

        if (body instanceof shinhands.com.model.daml.Error) {
            messages.addAll(((shinhands.com.model.daml.Error) body).getErrors());
        }

        if (message != null) {
            messages.add(message);
        }
        Error error = new Error(id, description, messages);
        log.error("=>", message);
        return new ErrorResponse(error);
    }

}
