package providers;

import data.CurrentUser;
import data.ServerArgument;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import request.LoginRequest;
import request.SignupRequest;
import response.JwtResponse;
import response.SignupResponse;

import java.io.IOException;
import java.util.List;

public class ServerConnectionProvider {
    private static ServerConnectionProvider instance;

    public static final String serverURL = "http://localhost:8080/";

    public static ServerConnectionProvider getInstance() {
        if(instance == null) instance = new ServerConnectionProvider();
        return instance;
    }

    private ServerConnectionProvider(){}

    public ResponseEntity<JwtResponse> loginRequest(LoginRequest requestEntity){
        var url = serverURL + "login";
        RestTemplate restTempl = new RestTemplate();
        //return restTempl.postForEntity(url, requestEntity, JwtResponse.class);
        return  restTempl.postForEntity(url, requestEntity, JwtResponse.class);
    }

    public ResponseEntity<SignupResponse> signUpRequest(SignupRequest requestEntity){

        var url = serverURL + "adminSignUp";
        RestTemplate restTempl = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.POST, new HttpEntity<>(headers), SignupResponse.class);
    }


    private String createURL(String serverURL, String serverFunction, List<ServerArgument> arguments){
        StringBuffer url = new StringBuffer();
        url.append(ServerConnectionProvider.serverURL);
        url.append("/");
        url.append(serverFunction);
        url.append("?");

        for(int i = 0 ; i < arguments.size() ; i++){
            url.append(arguments.get(i).toServerStyleString());
            if(i + 1 < arguments.size()){
                url.append("&");
            }
        }

        return url.toString();
    }

}
