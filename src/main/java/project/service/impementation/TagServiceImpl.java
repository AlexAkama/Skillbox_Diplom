package project.service.impementation;

import org.springframework.stereotype.Service;
import project.exception.ObjectNotFoundException;
import project.model.*;
import project.repository.TagRepository;
import project.repository.TagToPostRepository;
import project.service.TagService;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagToPostRepository tagToPostRepository;

    public TagServiceImpl(TagRepository tagRepository,
                          TagToPostRepository tagToPostRepository) {
        this.tagRepository = tagRepository;
        this.tagToPostRepository = tagToPostRepository;
    }

    @Override
    public void addTagsToPost(String[] tags, Post post) throws ObjectNotFoundException {
        for (String tagName : tags) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseThrow(() -> new ObjectNotFoundException(String.format("Tag %s not found", tagName)));
            if (!tagToPostRepository.existsByTagAndPost(tag, post)) {
                TagToPost tagToPost = new TagToPost(tag, post);
                tagToPostRepository.save(tagToPost);
            }
        }
    }

}
