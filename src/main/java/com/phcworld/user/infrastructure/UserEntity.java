package com.phcworld.user.infrastructure;

import com.phcworld.common.utils.FileConvertUtils;
import com.phcworld.common.utils.LocalDateTimeUtils;
import com.phcworld.user.domain.Authority;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@ToString(exclude = "password")
@Table(name = "USERS")
@DynamicUpdate
public class UserEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	private String password;

	private String name;

	@Enumerated(EnumType.STRING)
	private Authority authority;

	private LocalDateTime createDate;

	private String profileImage;

	private Boolean isDeleted;
	
	public String getFormattedCreateDate() {
		return LocalDateTimeUtils.getTime(createDate);
	}

	public String getProfileImageData(){
		return FileConvertUtils.getFileData(profileImage);
	}
	public String getProfileImageUrl(){
		return "http://localhost:8080/image/" + profileImage;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserEntity user = (UserEntity) o;
		return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(name, user.name) && authority == user.authority;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, email, name, authority);
	}

	public void modify(String password, String name, String profileImage) {
		this.password = password;
		this.name = name;
		this.profileImage = profileImage;
	}

	public void delete() {
		this.isDeleted = true;
	}
}
