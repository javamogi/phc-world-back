package com.phcworld.file.service;

import com.phcworld.exception.model.NotFoundException;
import com.phcworld.file.domain.FileType;
import com.phcworld.file.domain.UploadFile;
import com.phcworld.file.repository.UploadFileRepository;
import com.phcworld.user.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadFileService {
    private final UploadFileRepository uploadFileRepository;

    public String registerFile(Long postId, String imgName, String imgData, FileType fileType) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedBytes = decoder.decode(imgData);

        String fileExtension = imgName.substring(imgName.lastIndexOf("."));
        String randName = UUID.randomUUID().toString().replace("-", "") + fileExtension;

        // 임시 업로드 폴더
        // 추후 aws s3 연동 또는 다른 곳으로
        String filePath = "src/main/resources/static/";
        File file = new File(filePath + randName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(decodedBytes);
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UploadFile uploadFile = UploadFile.builder()
                .randFileName(randName)
                .originalFileName(imgName)
                .filePath(filePath)
                .isDelete(false)
                .fileType(fileType)
                .postId(postId)
                .build();

        uploadFileRepository.save(uploadFile);
        return randName;
    }
}
