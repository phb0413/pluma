package com.park.pluma.repository;

import com.park.pluma.entity.Post;
import com.park.pluma.entity.PostLike;
import com.park.pluma.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {

    long countByPost(Post post);

    Optional<PostLike> findByPostAndUser(Post post, User user);
}
