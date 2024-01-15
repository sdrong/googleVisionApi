package com.example.google.api.controller;

import com.example.google.api.dto.ImageAnalysisResult;
import com.example.google.api.service.GoogleVisionOCR;
import com.example.google.api.service.GoogleVisionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ImgController {

  //  private final GoogleVisionOCR googleVisionOCR;
    private final GoogleVisionService googleVisionService;
/*
    // 생성자를 통한 GoogleVisionOCR 서비스 주입
    public ImgController(GoogleVisionOCR googleVisionOCR) {
        this.googleVisionOCR = googleVisionOCR;
    }
*/
    // 생성자를 통한 GoogleVisionOCR 서비스 주입
    public ImgController(GoogleVisionService googleVisionService) {
        this.googleVisionService = googleVisionService;
    }

    @PostMapping(path = "/parse-image", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> parseImageByGoogleVision(@RequestParam("file") MultipartFile file) {
        /*
        try {

        //    String parsed = googleVisionOCR.execute(file.getBytes());
            String parsed = googleVisionService.execute(file.getBytes());
            ResponseData<String> responseData = new ResponseData<>(true, HttpStatus.OK, parsed);

            // 로그를 찍어서 responseData 객체 상태 확인
            System.out.println("ResponseData: " + responseData.toString());

            return ResponseEntity.ok().body(responseData);
        } catch (IOException e) {
            ResponseData<String> responseData = new ResponseData<>(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

            // 오류 로그 출력
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
         */
        try {
            ImageAnalysisResult analysisResult = googleVisionService.execute(file.getBytes());
            ResponseData<ImageAnalysisResult> responseData = new ResponseData<>(true, HttpStatus.OK, analysisResult);
            return ResponseEntity.ok(responseData);
        } catch (IOException e) {
            ResponseData<String> errorResponse = new ResponseData<>(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // 응답 데이터를 위한 클래스
    public static class ResponseData<T> {
        private boolean success;
        private HttpStatus status;
        private T data;

        // 기본 생성자 추가
        public ResponseData() {
        }

        // 모든 인자를 받는 생성자
        public ResponseData(boolean success, HttpStatus status, T data) {
            this.success = success;
            this.status = status;
            this.data = data;
        }

        // 게터와 세터 추가
        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public HttpStatus getStatus() {
            return status;
        }

        public void setStatus(HttpStatus status) {
            this.status = status;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
}
