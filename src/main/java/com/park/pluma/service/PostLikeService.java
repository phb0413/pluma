package com.park.pluma.service;

import com.park.pluma.entity.Post;
import com.park.pluma.entity.PostLike;
import com.park.pluma.entity.User;
import com.park.pluma.repository.PostLikeRepository;
import com.park.pluma.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    public long toggleLike(Long postId, User user) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        Optional<PostLike> like = postLikeRepository.findByPostAndUser(post, user);

        if(like.isPresent()) {
            postLikeRepository.delete(like.get());
        } else {
            PostLike newLike = PostLike.builder().post(post).user(user).build();

            postLikeRepository.save(newLike);
        }

        return postLikeRepository.countByPost(post);
    }



}
