package com.phcworld.file.service;

import com.phcworld.exception.model.NotFoundException;
import com.phcworld.file.domain.FileType;
import com.phcworld.file.domain.UploadFile;
import com.phcworld.file.repository.UploadFileRepository;
import com.phcworld.user.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${file.path}")
    private String filePath;

    public String registerFile(Long postId, String imgName, String imgData, FileType fileType) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedBytes = decoder.decode(imgData);

        String fileExtension = imgName.substring(imgName.lastIndexOf("."));
        String randName = UUID.randomUUID().toString().replace("-", "") + fileExtension;

        // 임시 업로드 폴더
        // 추후 aws s3 연동 또는 다른 곳으로
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
                .size(decodedBytes.length)
                .build();

        uploadFileRepository.save(uploadFile);
        return randName;
    }

    public String registerImages(String contents) {
        int idx = contents.indexOf("data:image/");
        while(idx != -1){
            String img = contents.substring(idx, contents.indexOf("\"", idx));
            String fileExtension = img.substring(img.indexOf("/") + 1, img.indexOf(";"));
            String randName = UUID.randomUUID().toString().replace("-", "") + "." + fileExtension;
            contents = contents.replace(img, "http://localhost:8080/image/" + randName);
            idx = contents.indexOf("data:image/", idx + 1);
            String imgData = img.substring(img.indexOf(",") + 1);
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decodedBytes = decoder.decode(imgData);
            File file = new File(filePath + randName);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(decodedBytes);
                fileOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        return contents;

    }
}
