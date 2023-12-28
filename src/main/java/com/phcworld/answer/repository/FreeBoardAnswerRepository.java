package com.phcworld.answer.repository;


import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FreeBoardAnswerRepository extends JpaRepository<FreeBoardAnswer, Long> {
	List<FreeBoardAnswer> findByWriter(User user);
}
