package kattsyn.dev.StatService.services;

import kattsyn.dev.StatService.entities.Like;
import kattsyn.dev.StatService.repositories.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    public Optional<Like> findByPostIdAndUserId(Long postId, Long userId) {
        return likeRepository.findByPostIdAndUserId(postId, userId);
    }

    public Like save(Like like) {
        return likeRepository.save(like);
    }
}
