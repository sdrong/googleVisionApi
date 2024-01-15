package com.example.google.api.dto;

import com.google.cloud.vision.v1.ColorInfo;
import com.google.cloud.vision.v1.EntityAnnotation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageAnalysisResult {
    private List<LabelAnnotation> labelAnnotations;
    private List<LocalizedObjectAnnotation> localizedObjectAnnotations;
    private List<ColorInfoDTO> colorInfos;
    public void setLabelAnnotationsFromEntityAnnotations(List<EntityAnnotation> entityAnnotations) {
        this.labelAnnotations = entityAnnotations.stream()
                .map(entity -> new LabelAnnotation(entity.getDescription(), entity.getScore()))
                .collect(Collectors.toList());
    }

    public void setLocalizedObjectAnnotationsFromEntityAnnotations(List<LocalizedObjectAnnotation> localAnnotations) {
        this.localizedObjectAnnotations = localAnnotations; // 이 경우, LocalizedObjectAnnotation 타입이 맞다고 가정합니다.
    }

    public void setColorInfos(List<ColorInfoDTO> colorInfos) {
        this.colorInfos = colorInfos;
    }
}
