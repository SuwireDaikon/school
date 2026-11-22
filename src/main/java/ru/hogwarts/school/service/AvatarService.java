package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;

import java.util.stream.IntStream;

@Service
public class AvatarService {

    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    private final AvatarRepository avatarRepository;

    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    public Page<Avatar> getAllAvatars(int page, int size) {
        logger.info("Was invoked method for get all avatars with pagination");
        return avatarRepository.findAll(PageRequest.of(page, size));
    }

    public int calculate() {
        logger.info("calculation...");
        return IntStream.rangeClosed(1, 1_000_000)
                .parallel()
                .reduce(0, Integer::sum);
    }
}
