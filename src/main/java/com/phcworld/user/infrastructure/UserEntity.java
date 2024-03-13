package com.phcworld.user.infrastructure;

import com.phcworld.common.utils.FileConvertUtils;
import com.phcworld.common.utils.LocalDateTimeUtils;
import com.phcworld.user.domain.Authority;
import com.phcworld.user.domain.User;
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

//	public String getFormattedCreateDate() {
//		return LocalDateTimeUtils.getTime(createDate);
//	}
//
//	public String getProfileImageData(){
//		return FileConvertUtils.getFileData(profileImage);
//	}
//	public String getProfileImageUrl(){
//		return "http://localhost:8080/image/" + profileImage;
//	}

	public static UserEntity from(User user) {
		return UserEntity.builder()
				.id(user.getId())
				.email(user.getEmail())
				.password(user.getPassword())
				.name(user.getName())
				.authority(user.getAuthority())
				.createDate(user.getCreateDate())
				.profileImage(user.getProfileImage())
				.isDeleted(user.isDeleted())
				.build();
	}

	public User toModel() {
		return User.builder()
				.id(id)
				.email(email)
				.name(name)
				.authority(authority)
				.profileImage(profileImage)
				.createDate(createDate)
				.isDeleted(isDeleted)
				.build();
	}
}
