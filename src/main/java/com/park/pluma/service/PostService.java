package com.park.pluma.service;

import com.park.pluma.dto.PostRequest;
import com.park.pluma.dto.PostResponse;
import com.park.pluma.entity.Post;
import com.park.pluma.entity.User;
import com.park.pluma.repository.PostRepository;
import com.park.pluma.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
}
