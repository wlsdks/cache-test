package com.study.cache.repository;

import com.study.cache.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Query(value = "SELECT p FROM PostEntity p ORDER BY p.id ASC")
    List<PostEntity> findAllByOffsetAndLimit(int offset, int limit);

}