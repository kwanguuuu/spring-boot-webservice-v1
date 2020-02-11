package com.springboot.peter.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//보통 mybatis 에서 dao라고 불리는 db layer
public interface PostsRepository extends JpaRepository<Posts,Long> {
    @Query("SELECT p from Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
}
