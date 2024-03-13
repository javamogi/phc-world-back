package com.phcworld.freeboard.repository;


import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.user.infrastructure.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long>, FreeBoardRepositoryCustom {
	List<FreeBoard> findByWriter(UserEntity user);

	@Query("select f from FreeBoard f join fetch f.writer")
	List<FreeBoard> findAllByFetch();
}
