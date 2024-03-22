package com.phcworld.answer.infrastructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phcworld.answer.domain.FreeBoardAnswer;
import com.phcworld.freeboard.infrastructure.FreeBoardEntity;
import com.phcworld.user.infrastructure.UserEntity;
import com.phcworld.common.utils.LocalDateTimeUtils;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "free_board_answer",
		indexes = {@Index(name = "idx__writer_id_create_date", columnList = "writer_id, createDate"),
		@Index(name = "idx__free_board_id_create_date", columnList = "free_board_id, createDate")})
public class FreeBoardAnswerEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_freeBoardAnswer_writer"))
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private UserEntity writer;

	@ManyToOne
	@JoinColumn(name = "free_board_id", foreignKey = @ForeignKey(name = "fk_freeBoardAnswer_to_freeBoard"), nullable = false)
	private FreeBoardEntity freeBoardEntity;
	
	@Lob
	private String contents;

	@CreatedDate
	private LocalDateTime createDate;
	
	@LastModifiedDate
	private LocalDateTime updateDate;

	private Boolean isDeleted;

	public static FreeBoardAnswerEntity from(FreeBoardAnswer freeBoardAnswer) {
		return FreeBoardAnswerEntity.builder()
				.id(freeBoardAnswer.getId())
				.writer(UserEntity.from(freeBoardAnswer.getWriter()))
				.freeBoardEntity(FreeBoardEntity.from(freeBoardAnswer.getFreeBoard()))
				.contents(freeBoardAnswer.getContents())
				.createDate(freeBoardAnswer.getCreateDate())
				.updateDate(freeBoardAnswer.getUpdateDate())
				.isDeleted(freeBoardAnswer.isDeleted())
				.build();
	}

	public FreeBoardAnswer toModel() {
		return FreeBoardAnswer.builder()
				.id(id)
				.writer(writer.toModel())
				.freeBoard(freeBoardEntity.toModelWithoutAnswers())
				.contents(contents)
				.createDate(createDate)
				.updateDate(updateDate)
				.isDeleted(isDeleted)
				.build();
	}
}
