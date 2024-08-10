package kattsyn.dev.SocialNetwork.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class AppException extends Exception {
    private final HttpStatus status;
    private final LocalDateTime timestamp;

    public AppException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

}
