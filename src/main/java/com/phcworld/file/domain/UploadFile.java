package com.phcworld.file.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFileName;

    private String randFileName;

    private String filePath;

    private Boolean isDelete;

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    private Long postId;
}
