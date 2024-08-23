package kattsyn.dev.PostService.exceptions;

import com.google.protobuf.Any;
import com.google.protobuf.Timestamp;
import com.google.rpc.Code;
import com.google.rpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import io.grpc.reflection.v1alpha.ErrorResponse;
import kattsyn.dev.grpc.PostServiceErrorCode;
import kattsyn.dev.grpc.PostServiceExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

import java.time.Instant;

@GrpcAdvice
@Slf4j
public class PostServiceExceptionHandler {

    @GrpcExceptionHandler
    public StatusRuntimeException handle(PostServiceException exception) {
        log.error(exception.getMessage(), exception);
        Instant instant = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();

        PostServiceExceptionResponse exceptionResponse = PostServiceExceptionResponse.newBuilder()
                .setErrorCode(PostServiceErrorCode.values()[exception.getErrorCode().ordinal()])
                .setTimestamp(timestamp)
                .build();

        Status status = Status.newBuilder()
                .setCode(Code.NOT_FOUND_VALUE)
                .setMessage(exception.getMessage())
                .addDetails(Any.pack(exceptionResponse))
                .build();
/*
        return PostServiceExceptionResponse.newBuilder()
                .setErrorCode(PostServiceErrorCode.values()[exception.getErrorCode().ordinal()])
                .setTimestamp(timestamp)
                .build();

 */
        return StatusProto.toStatusRuntimeException(status);
    }
}
