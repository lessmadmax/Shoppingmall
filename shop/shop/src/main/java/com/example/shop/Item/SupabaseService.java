package com.example.shop.Item;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class SupabaseService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.service-key}")
    private String serviceKey;

    @Value("${supabase.bucket}")
    private String bucketName;

    private final OkHttpClient client = new OkHttpClient();

    /**
     * Supabase Presigned URL 생성 메서드
     * @param path - 업로드할 파일 경로 (예: "uploads/image.jpg")
     * @return Presigned URL
     * @throws IOException
     */
    public String createPresignedUrl(String path) throws IOException {
        // Supabase Presigned URL 요청 URL
        String requestUrl = supabaseUrl + "/storage/v1/object/upload/sign/" + bucketName + "/" + path + "?expiresIn=180";

        System.out.println("Presigned URL Request: " + requestUrl);

        Request request = new Request.Builder()
                .url(requestUrl)
                .addHeader("Authorization", "Bearer " + serviceKey)
                .addHeader("apikey", serviceKey)
                .build();



        try (Response response = client.newCall(request).execute()) {
            System.out.println("🔹 Supabase API 응답 상태 코드: " + response.code()); // 응답 코드 출력
            if (response.body() != null) {
                String responseBody = response.body().string();
                System.out.println("🔹 Supabase API 응답 본문: " + responseBody); // 응답 본문 출력

                if (response.isSuccessful()) {
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    return jsonResponse.getString("signedURL"); // Supabase가 반환하는 Presigned URL
                } else {
                    throw new IOException("Failed to get presigned URL: " + response.message());
                }
            } else {
                throw new IOException("Supabase API 응답이 null입니다.");
            }
        }
    }
}

