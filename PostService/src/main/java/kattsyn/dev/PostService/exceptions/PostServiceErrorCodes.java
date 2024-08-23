package kattsyn.dev.PostService.exceptions;

import lombok.Getter;

@Getter
public enum PostServiceErrorCodes {
    NOT_FOUND(404),
    FORBIDDEN(403);

    private int code;

    PostServiceErrorCodes(int code) {}

}

