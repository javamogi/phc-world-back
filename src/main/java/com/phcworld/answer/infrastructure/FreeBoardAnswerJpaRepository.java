package com.phcworld.answer.infrastructure;


import com.phcworld.user.infrastructure.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FreeBoardAnswerJpaRepository extends JpaRepository<FreeBoardAnswerEntity, Long> {
	List<FreeBoardAnswerEntity> findByWriter(UserEntity user);
}
