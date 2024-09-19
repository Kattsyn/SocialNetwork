package kattsyn.dev.StatService.services;

import kattsyn.dev.StatService.entities.Like;
import kattsyn.dev.StatService.repositories.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    public Like save(Like like) {
        return likeRepository.save(like);
    }
}
