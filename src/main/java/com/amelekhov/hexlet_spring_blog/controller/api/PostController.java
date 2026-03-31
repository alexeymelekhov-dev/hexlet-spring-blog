package com.amelekhov.hexlet_spring_blog.controller.api;

import com.amelekhov.hexlet_spring_blog.exception.ResourceNotFoundException;
import com.amelekhov.hexlet_spring_blog.model.Post;
import com.amelekhov.hexlet_spring_blog.repository.PostRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<Post> getPublishedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByPublishedTrue(pageable);
    }

    @PostMapping()
    public ResponseEntity<Post> createPost(@Valid @RequestBody Post post) {
        Post saved = postRepository.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found id: " + id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody Post data
    ) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found id: " + id));

        post.setTitle(data.getTitle());
        post.setContent(data.getContent());
        post.setPublished(data.getPublished());

        Post saved = postRepository.save(post);

        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found id: " + id));;

        postRepository.deleteById(post.getId());
    }
}
