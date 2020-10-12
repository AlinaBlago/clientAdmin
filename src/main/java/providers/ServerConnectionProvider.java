package providers;

import data.CurrentUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import request.UserRequest;
import request.LoginRequest;
import request.SignupRequest;
import response.ChatResponse;
import response.FindUserResponse;
import response.SignupResponse;
import response.UserResponse;

public class ServerConnectionProvider {
    private static ServerConnectionProvider instance;

    public static final String serverURL = "http://localhost:8080/";

    public static RestTemplate restTempl = new RestTemplate();

    public static ServerConnectionProvider getInstance() {
        if(instance == null) instance = new ServerConnectionProvider();
        return instance;
    }

    private ServerConnectionProvider(){}

    public ResponseEntity<String> loginRequest(LoginRequest requestEntity) {
        var url = serverURL + "login";
        return restTempl.postForEntity(url, requestEntity, String.class);
    }

    public ResponseEntity<SignupResponse> signUpRequest(SignupRequest requestEntity){
        var url = serverURL + "users/admins/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.POST, new HttpEntity<>(requestEntity, headers), SignupResponse.class);
    }

    public ResponseEntity<FindUserResponse> findUser(UserRequest request) {
        var url = serverURL + "users/admins/find";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), FindUserResponse.class);
    }

    public ResponseEntity<UserResponse> addChat(UserRequest request) {
        var url = serverURL + "users/admins/chats";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), UserResponse.class);
    }

    public ResponseEntity<String> deleteUser(UserRequest request){
        var url = serverURL + "users/admins/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.DELETE, new HttpEntity<>(request, headers), String.class);
    }

    public ResponseEntity<String> lockUser(UserRequest request){
        var url = serverURL + "users/admins";
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(180000);
        requestFactory.setReadTimeout(180000);

        restTempl.setRequestFactory(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.PATCH, new HttpEntity<>(request, headers), String.class);
    }

    public ResponseEntity<String> unLockUser(UserRequest request){
        var url = serverURL + "users/admins/unlock";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), String.class);
    }
}
