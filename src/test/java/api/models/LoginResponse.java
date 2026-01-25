package api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {
    private String userId;
    private String userName;
    private String token;
    private String expires;
    private String status;
    private String result;
}


