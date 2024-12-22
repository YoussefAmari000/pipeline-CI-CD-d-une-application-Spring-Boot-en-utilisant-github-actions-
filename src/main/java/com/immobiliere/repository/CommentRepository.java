package com.immobiliere.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.immobiliere.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByHouseId(Long houseId);

	
}
