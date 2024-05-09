package fit.api.social_network.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import fit.api.social_network.model.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String BAD_REQUEST = "BAD REQUEST";
    final ObjectMapper mapper = new ObjectMapper();
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ApiResponse<String>> globalExceptionHandler(NotFoundException ex) {
        log.error(""+ex.getMessage(), ex);
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        apiMessageDto.error(ex.getMessage());
        return new ResponseEntity<>(apiMessageDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        apiMessageDto.error(ex.getMessage());
        return new ResponseEntity<>(apiMessageDto, HttpStatus.FORBIDDEN);
    }

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ApiResponse<String> exceptionHandler(Exception ex) {
        log.error(""+ex.getMessage(), ex);
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        apiMessageDto.error(ex.getMessage());
		return apiMessageDto;
	}


    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ApiResponse<String>> badRequest(BadRequestException ex) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        apiMessageDto.error(ex.getMessage());
        return new ResponseEntity<>(apiMessageDto, HttpStatus.BAD_REQUEST);
    }

}
