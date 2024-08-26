package kattsyn.dev.PostService.exceptions;

import lombok.Getter;

@Getter
public class PostServiceException extends RuntimeException {

    private final PostServiceErrorCodes errorCode;
    private final String message;

    public PostServiceException(PostServiceErrorCodes errorCode, String message) {
        super(errorCode.name());
        this.errorCode = errorCode;
        this.message = message;
    }
}
