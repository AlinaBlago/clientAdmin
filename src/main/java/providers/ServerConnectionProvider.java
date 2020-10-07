package providers;

import data.CurrentUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import request.AddChatRequest;
import request.LoginRequest;
import request.SignupRequest;
import response.ChatResponse;
import response.SignupResponse;
import response.UserResponse;

public class ServerConnectionProvider {
    private static ServerConnectionProvider instance;

    public static final String serverURL = "http://localhost:8080/";

    public static ServerConnectionProvider getInstance() {
        if(instance == null) instance = new ServerConnectionProvider();
        return instance;
    }

    private ServerConnectionProvider(){}

    public ResponseEntity<String> loginRequest(LoginRequest requestEntity) {
        var url = serverURL + "login";
        RestTemplate restTempl = new RestTemplate();
        return restTempl.postForEntity(url, requestEntity, String.class);
    }

    public ResponseEntity<SignupResponse> signUpRequest(SignupRequest requestEntity){
        var url = serverURL + "users/admins/";
        RestTemplate restTempl = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.POST, new HttpEntity<>(requestEntity, headers), SignupResponse.class);
    }

    public ResponseEntity<ChatResponse> addChat(AddChatRequest request) {
        var url = serverURL + "users/admins/chats";
        RestTemplate restTempl = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), ChatResponse.class);
    }


}
