/*
package com.example.google.api.service;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ImgService {
    // 이미지 파일이 저장될 디렉토리 경로 (자신의 로컬 환경에 맞게 수정해야함)
    private static String DIR_PATH =  "/Users/mingeun/Vibecap/prototype01/capturedImgs/";

    // 이미지 파일을 로컬에 저장
    public String saveFile(MultipartFile file) throws IOException, IllegalStateException {

        if (file.isEmpty())
            return null;

        String originalName = file.getOriginalFilename();                               // 파일 원래 이름
        String uuid = UUID.randomUUID().toString();                                     // 파일 식별자
        String extension = originalName.substring(originalName.lastIndexOf("."));   // 파일 확장자 추출
        String savedName = uuid + extension;                                            // 이미지 파일의 새로운 이름
        String savedPath = DIR_PATH + savedName;                                         // 파일 경로

        file.transferTo(new File(savedPath));                                           // local에 파일 저장

        return savedPath;
    }

    // 이미지 파일에 대한 키워드를 하나의 문자열로 반환
    public String extractKeywords(String imgFilePath) throws Exception {

        AtomicReference<String> labels = new AtomicReference<>("");

        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

            // Reads the image file into memory
            Path path = Paths.get(imgFilePath);
            byte[] data = Files.readAllBytes(path);
            ByteString imgBytes = ByteString.copyFrom(data);

            // Builds the image annotation request
            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
            AnnotateImageRequest request =
                    AnnotateImageRequest.newBuilder()
                            .addFeatures(feat)
                            .setImage(img)
                            .build();
            requests.add(request);

            // Performs label detection on the image file
            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("[ERROR]: %s%n", res.getError().getMessage());
                    return null;
                }

                List<ImgDescription> keywords;
                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    annotation
                            .getAllFields()
                            .forEach((k, v) -> {
//                                List<String> fieldNames = List.of(k.toString().split("."));
//                                System.out.format("%-16s : %s\n", fieldNames.get(fieldNames.size()), v.toString());
                                if (k.toString().contains("description")) {
                                    System.out.format("%s, ", v.toString());
                                    labels.set(labels + v.toString() + "\n");
                                }
                            });
                }
            }
        }

        return labels.toString();
    }

}*/
