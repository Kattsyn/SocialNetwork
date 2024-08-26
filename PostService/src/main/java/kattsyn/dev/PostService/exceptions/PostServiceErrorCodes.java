package kattsyn.dev.PostService.exceptions;

import lombok.Getter;

@Getter
public enum PostServiceErrorCodes {
    NOT_FOUND(404),
    FORBIDDEN(403);

    private final int code;

    PostServiceErrorCodes(int code) {
        this.code = code;
    }

}

