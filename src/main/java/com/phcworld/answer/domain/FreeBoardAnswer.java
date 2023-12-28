package com.phcworld.answer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phcworld.freeboard.domain.FreeBoard;
import com.phcworld.user.domain.User;
import com.phcworld.utils.LocalDateTimeUtils;
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
@Table(indexes = {@Index(name = "idx__writer_id_create_date", columnList = "writer_id, createDate"),
		@Index(name = "idx__free_board_id_create_date", columnList = "free_board_id, createDate")})
public class FreeBoardAnswer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_freeBoardAnswer_writer"))
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private User writer;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_freeBoardAnswer_to_freeBoard"), nullable = false)
	private FreeBoard freeBoard;
	
	@Lob
	private String contents;

	@CreatedDate
	private LocalDateTime createDate;
	
	@LastModifiedDate
	private LocalDateTime updateDate;
	
	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(createDate);
	}
	
	public String getFormattedUpdateDate() {
		return LocalDateTimeUtils.getTime(updateDate);
	}

	public boolean isSameWriter(Long userId) {
		return this.writer.getId().equals(userId);
	}

	public void update(String contents) {
		this.contents = contents;
	}

}
