package com.phcworld.freeboard.infrastructure;


import com.phcworld.user.infrastructure.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FreeBoardJpaRepository extends JpaRepository<FreeBoardEntity, Long>, FreeBoardRepositoryCustom {
	List<FreeBoardEntity> findByWriter(UserEntity user);

	@Query("select f from FreeBoardEntity f join fetch f.writer")
	List<FreeBoardEntity> findAllByFetch();
}
