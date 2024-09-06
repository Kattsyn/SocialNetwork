package kattsyn.dev.StatService.dtos;

import kattsyn.dev.StatService.enums.Events;
import lombok.Data;


@Data
public class EventCreationDto {
    private Events type;
    private Long userId;
    private Long postId;

    public EventCreationDto(Events type, Long userId, Long postId) {
        this.type = type;
        this.userId = userId;
        this.postId = postId;
    }
}
