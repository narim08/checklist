package rabo.checklist.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import rabo.checklist.domain.ChallengeChecklist;
import rabo.checklist.domain.Location;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiaryService {

    @Value("${kakao.map.api.url}")
    private String KAKAO_API_URL;

    @Value("${kakao.maps.restapi.key}")
    private String kakaoMapsRestApiKey;

    public String generateDiary(ChallengeChecklist checklist) {

        //프롬프트 생성
        StringBuilder prompt = new StringBuilder();
        String dateStr = checklist.getDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));

        prompt.append("날짜: ").append(dateStr);
        prompt.append("\n");

        //체크리스트 정보 추가
        prompt.append("오늘은 ");
        if (checklist.isAllCompleted()) {
            prompt.append("공부, 산책, 운동 모두 완료했어! 대단한데?");
        } else if (checklist.isTask1Completed() && checklist.isTask2Completed() && !checklist.isTask3Completed()) {
            prompt.append("공부, 산책 완료! 열심히 공부하고 밖에도 나갔으니 이 정도면 잘했어~! 내일은 운동도 해보자!!");
        } else if (checklist.isTask1Completed() && !checklist.isTask2Completed() && checklist.isTask3Completed()) {
            prompt.append("공부, 운동 완료! 집에서 뿌듯한 하루를 보냈는걸? 내일은 바깥에도 나가보자!!");
        } else if (!checklist.isTask1Completed() && checklist.isTask2Completed() && checklist.isTask3Completed()) {
            prompt.append("산책, 운동 완료! 오늘은 수련 데이인가? 열심히 하루를 보냈으니 만점이야!");
        } else if (checklist.isTask1Completed() && !checklist.isTask2Completed() && !checklist.isTask3Completed()) {
            prompt.append("공부만 완료! 열심히 공부해서 그런가 어쩐지 몸이 피곤한 걸... 내일은 몸도 좀 풀자!");
        } else if (!checklist.isTask1Completed() && checklist.isTask2Completed() && !checklist.isTask3Completed()) {
            prompt.append("산책만 완료! 밖에라도 나갔으니 꽤 괜찮은 하루네! 재밌게 놀았으면 됐어~");
        } else if (!checklist.isTask1Completed() && !checklist.isTask2Completed() && checklist.isTask3Completed()) {
            prompt.append("운동만 완료! 훈련이라도 했으니 대단한데? 앞으로도 팟팅!!");
        } else {
            prompt.append("잠시 쉬어가는 날이야~ 충전 중...");
        }


        //위치 정보 추가
        if (!checklist.getLocations().isEmpty()) {
            prompt.append("\n\n오늘 갔다왔던 장소들은 여기야!\n");
            List<Location> locations = checklist.getLocations();
            for (Location location : locations) {
                String address = getAddress(location.getLatitude(), location.getLongitude());
                prompt.append("-");
                prompt.append(address);
                prompt.append("\n");
            }
        } else {
            prompt.append("\n");
        }
        prompt.append("\n하루 정리 끝! 총평: 언제나 별점 만점이야\n");
        return prompt.toString();
    }

    public String getAddress(double latitude, double longitude) {

        //요청 URL 생성
        String url = UriComponentsBuilder.fromHttpUrl(KAKAO_API_URL)
                .queryParam("x", longitude)
                .queryParam("y", latitude)
                .toUriString();

        //HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoMapsRestApiKey);

        //REST API 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,
                new org.springframework.http.HttpEntity<>(headers), String.class);

        // JSON 응답에서 주소 추출
        String responseBody = response.getBody();
        if (responseBody != null) {
            return extractAddressFromJson(responseBody);
        }
        return "주소를 찾을 수 없습니다.";
    }

    // JSON 응답에서 주소 추출
    private static String extractAddressFromJson(String responseBody) {
        try {
            // Jackson 라이브러리를 사용해 JSON 파싱
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode documents = rootNode.path("documents");

            // 첫 번째 문서에서 주소 정보 추출
            if (documents.isArray() && !documents.isEmpty()) {
                JsonNode addressNode = documents.get(0).path("address");
                return addressNode.path("address_name").asText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "주소를 찾을 수 없습니다.";
    }


    /*
    @Value("${huggingface.api.url}")
    private String apiUrl;
    @Value("${huggingface.api.token}")
    private String apiToken;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    public DiaryService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;

    public String generateDiary(ChallengeChecklist checklist) {
        String prompt = buildPrompt(checklist);

        try {
            // API 요청 준비
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("inputs", prompt);
            requestBody.put("parameters", Map.of(
                    "max_length", 512,
                    "temperature", 0.7,
                    "top_p", 0.9
            ));

            HttpEntity<String> request = new HttpEntity<>(
                    objectMapper.writeValueAsString(requestBody),
                    headers
            );

            // API 요청 디버깅
            System.out.println("API request body: " + objectMapper.writeValueAsString(requestBody));

            // API 호출 및 응답 처리
            ResponseEntity<List<Map<String, String>>> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<List<Map<String, String>>>() {}
            );
            System.out.println("API response: " + response.getBody()); // 응답 디버깅

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map<String, String>> responseBody = response.getBody();
                System.out.println("API response: " + responseBody); // 디버깅용 로그

                if (!responseBody.isEmpty() && responseBody.get(0).containsKey("generated_text")) {
                    return responseBody.get(0).get("generated_text");
                }
            }

            return "Diary creation failed.";
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 처리 중 오류가 발생했습니다.", e);
        } catch (Exception e) {
            throw new RuntimeException("일기 생성 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    private String buildPrompt(ChallengeChecklist checklist) {
        //프롬프트 생성
        StringBuilder prompt = new StringBuilder();
        String dateStr = checklist.getDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        prompt.append("I'll tell you what the user did during the day, so please write a message of support along with a brief summary.\n\n");
        prompt.append("Today's date is ").append(dateStr);

        //체크리스트 정보 추가
        if (checklist.isTask1Completed() || checklist.isTask2Completed() || checklist.isTask3Completed()) {
            prompt.append("What the user did:\n");

            if (checklist.isTask1Completed()) prompt.append("-study: completed\n");
            if (checklist.isTask2Completed()) prompt.append("-Walk: completed\n");
            if (checklist.isTask3Completed()) prompt.append("-exercise: completed\n");
        }

        //위치 정보 추가
        if (!checklist.getLocations().isEmpty()) {
            prompt.append("\nPlaces the user walked:\n");
            checklist.getLocations().forEach(location ->
                    prompt.append(String.format("-Latitude %.6f, Longitude %.6f\n",
                            location.getLatitude(), location.getLongitude()
                    ))
            );
        }
        prompt.append("\nPlease fill out the form based on the information above.");
        return prompt.toString();
    }*/
}
