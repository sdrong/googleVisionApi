package com.example.google.api.service;

import com.example.google.api.dto.ImageAnalysisResult;
import com.example.google.api.dto.LocalizedObjectAnnotation;
import com.example.google.api.dto.ColorInfoDTO; // 색상 정보를 위한 DTO
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoogleVisionService {
    public ImageAnalysisResult execute(byte[] fileData) throws IOException {
        StopWatch totalTime = new StopWatch();
        totalTime.start();

        Image img = Image.newBuilder().setContent(ByteString.copyFrom(fileData)).build();

        Feature labelFeature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
        Feature objectLocalizationFeature = Feature.newBuilder().setType(Feature.Type.OBJECT_LOCALIZATION).build();
        Feature imagePropertiesFeature = Feature.newBuilder().setType(Feature.Type.IMAGE_PROPERTIES).build(); // 색상 정보 감지를 위한 기능

        AnnotateImageRequest labelRequest = AnnotateImageRequest.newBuilder().addFeatures(labelFeature).setImage(img).build();
        AnnotateImageRequest objectLocalizationRequest = AnnotateImageRequest.newBuilder().addFeatures(objectLocalizationFeature).setImage(img).build();
        AnnotateImageRequest imagePropertiesRequest = AnnotateImageRequest.newBuilder().addFeatures(imagePropertiesFeature).setImage(img).build(); // 색상 정보 요청

        List<AnnotateImageRequest> requests = new ArrayList<>();
        requests.add(labelRequest);
        requests.add(objectLocalizationRequest);
        requests.add(imagePropertiesRequest);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            ImageAnalysisResult result = new ImageAnalysisResult();
            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.printf("Error: %s%n", res.getError().getMessage());
                    continue;
                }

                List<LocalizedObjectAnnotation> localizedAnnotations = res.getLocalizedObjectAnnotationsList().stream()
                        .map(googleAnnotation -> new LocalizedObjectAnnotation(
                                googleAnnotation.getName(),
                                googleAnnotation.getScore()))
                        .collect(Collectors.toList());

                result.setLocalizedObjectAnnotations(localizedAnnotations);

                // 색상 정보 처리
                if (res.hasImagePropertiesAnnotation()) {
                    List<ColorInfoDTO> colorInfoDTOs = res.getImagePropertiesAnnotation().getDominantColors().getColorsList().stream()
                            .map(colorInfo -> new ColorInfoDTO(colorInfo.getColor().getRed(), colorInfo.getColor().getGreen(), colorInfo.getColor().getBlue(), colorInfo.getScore()))
                            .collect(Collectors.toList());
                    result.setColorInfos(colorInfoDTOs);
                }
            }

            totalTime.stop();
            System.out.println("Total Time: " + totalTime.getTotalTimeMillis() + "ms");
            return result;
        } catch (Exception e) {
            totalTime.stop();
            System.err.println("Error during image analysis: " + e.getMessage());
            System.err.println("Total Time: " + totalTime.getTotalTimeMillis() + "ms");
            throw new IOException("Error processing image analysis", e);
        }
    }
}
