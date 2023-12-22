package com.phcworld.freeboard.repository;


import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long>, FreeBoardRepositoryCustom {
	List<FreeBoard> findByWriter(User user);

	@Query("select f from FreeBoard f join fetch f.writer")
	List<FreeBoard> findAllByFetch();
}
