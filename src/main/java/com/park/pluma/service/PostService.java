package com.park.pluma.service;

import com.park.pluma.dto.PostRequest;
import com.park.pluma.dto.PostResponse;
import com.park.pluma.entity.Post;
import com.park.pluma.entity.User;
import com.park.pluma.repository.PostRepository;
import com.park.pluma.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponse createPost(PostRequest postRequest, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + username));

        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .author(user)
                .build();

        postRepository.save(post);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(user.getUsername())
                .createdAt(post.getCreatedAt())
                .build();

    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return new PostResponse(post);
    }

    @Transactional
    public PostResponse updatePost(Long id, PostRequest postRequest, User user) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        if(!post.getAuthor().getUsername().equals(user.getUsername())) {
            throw new RuntimeException("작성자만 수정 가능합니다.");
        }

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());

        return new PostResponse(postRepository.save(post));
    }

    @Transactional
    public void deletePost(Long id, User user) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        if(!post.getAuthor().getUsername().equals(user.getUsername())) {
            throw new RuntimeException("작성자만 삭제 가능합니다.");
        }

        postRepository.delete(post);
    }
}
