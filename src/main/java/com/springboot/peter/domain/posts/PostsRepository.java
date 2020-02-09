package com.springboot.peter.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

//보통 mybatis 에서 dao라고 불리는 db layer
public interface PostsRepository extends JpaRepository<Posts,Long> {

}
