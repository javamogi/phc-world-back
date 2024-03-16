package com.phcworld.freeboard.infrastructure;

import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.user.domain.User;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.common.utils.LocalDateTimeUtils;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Table(name = "free_board",
		indexes = {@Index(name = "idx__create_date", columnList = "createDate"),
				@Index(name = "idx__writer_id_create_date", columnList = "writer_id, createDate")})
//@SequenceGenerator(
//		name = "BOARD_SEQ_GENERATOR",
//		sequenceName = "BOARD_SEQ",
//		initialValue = 1, allocationSize = 10000
//)
public class FreeBoardEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_GENERATOR")
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_freeBoard_writer"))
	private UserEntity writer;

	private String title;

	@Lob
	private String contents;

	@CreatedDate
	private LocalDateTime createDate;
	
	@LastModifiedDate
	private LocalDateTime updateDate;

	@ColumnDefault("0")
	@Builder.Default
	private Integer count = 0;

	@ColumnDefault("false")
	private Boolean isDeleted = false;

	@OneToMany(mappedBy = "freeBoardEntity", cascade = CascadeType.REMOVE)
	private List<FreeBoardAnswer> freeBoardAnswers;

	public static FreeBoardEntity from(FreeBoard freeBoard){
		return FreeBoardEntity.builder()
				.id(freeBoard.getId())
				.writer(UserEntity.from(freeBoard.getWriter()))
				.title(freeBoard.getTitle())
				.contents(freeBoard.getContents())
				.createDate(freeBoard.getCreateDate())
				.updateDate(freeBoard.getUpdateDate())
				.count(freeBoard.getCount())
				.isDeleted(freeBoard.isDeleted())
				.build();
	}

	public FreeBoard toModel(){
		return FreeBoard.builder()
				.id(id)
				.writer(writer.toModel())
				.title(title)
				.contents(contents)
				.createDate(createDate)
				.updateDate(updateDate)
				.count(count)
				.isDeleted(isDeleted)
				.answers(freeBoardAnswers)
				.build();
	}
}
