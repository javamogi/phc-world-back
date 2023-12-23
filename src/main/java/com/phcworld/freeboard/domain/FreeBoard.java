package com.phcworld.freeboard.domain;

import com.phcworld.freeboard.dto.FreeBoardRequestDto;
import com.phcworld.user.domain.User;
import com.phcworld.utils.LocalDateTimeUtils;
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
public class FreeBoard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_GENERATOR")
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_freeBoard_writer"))
	private User writer;

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

//	@OneToMany(mappedBy = "freeBoard", cascade = CascadeType.REMOVE)
	// @JsonManagedReference
//	@JsonBackReference
//	private List<FreeBoardAnswer> freeBoardAnswers;

//	public String getCountOfAnswer() {
//		if (this.freeBoardAnswers == null|| this.freeBoardAnswers.size() == 0) {
//			return "";
//		}
//		return "[" + freeBoardAnswers.size() + "]";
//	}

	public void addCount() {
		this.count += 1;
	}

	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(createDate);
	}
	
	public String getFormattedUpdateDate() {
		return LocalDateTimeUtils.getTime(updateDate);
	}

	public void update(String title, String contents) {
		this.title = title;
		this.contents = contents;
	}

	public Boolean isNew(){
		final int HOUR_OF_DAY = 24;
		final int MINUTES_OF_HOUR = 60;

		long createdDateAndNowDifferenceMinutes =
				Duration.between(createDate == null ? LocalDateTime.now() : createDate, LocalDateTime.now()).toMinutes();
        return (createdDateAndNowDifferenceMinutes / MINUTES_OF_HOUR) < HOUR_OF_DAY;
	}

	public boolean matchUser(Long userId) {
		return !this.writer.getId().equals(userId);
	}

	public void delete() {
		this.isDeleted = true;
	}
}
