package kattsyn.dev.SocialNetwork.dtos;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private final HttpStatus status;
    private final String message;
    private final LocalDateTime time;

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.time = LocalDateTime.now();
    }
}
