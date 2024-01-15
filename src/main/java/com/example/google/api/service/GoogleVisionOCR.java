package com.example.google.api.service;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageSource;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class GoogleVisionOCR {
    public String execute(byte[] fileData) throws IOException {
        StopWatch totalTime = new StopWatch();
        totalTime.start();

        // 이미지 데이터 설정
        Image img = Image.newBuilder().setContent(ByteString.copyFrom(fileData)).build();

        // 텍스트 감지 기능 설정
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();

        // 요청 객체 생성
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();

        // 요청 목록에 추가
        List<AnnotateImageRequest> requests = new ArrayList<>();
        requests.add(request);

        // 클라이언트를 사용하여 API 호출
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            // 결과 문자열을 담을 빌더
            StringBuilder result = new StringBuilder();

            // 각 응답에 대해 텍스트 추출
            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return "Error: " + res.getError().getMessage();
                }

                // 첫 번째 텍스트 주석만 추출
                if (!res.getTextAnnotationsList().isEmpty()) {
                    EntityAnnotation annotation = res.getTextAnnotationsList().get(0);
                    result.append(annotation.getDescription());
                }
            }

            totalTime.stop();
            System.out.println("Total Time: " + totalTime.getTotalTimeMillis() + "ms");

            // 추출된 텍스트 반환
            return result.toString();
        } catch (Exception e) {
            totalTime.stop();
            System.out.println("Error during image annotation: " + e.getMessage());
            System.out.println("Total Time: " + totalTime.getTotalTimeMillis() + "ms");
            return "Error: " + e.getMessage();
        }
    }
}