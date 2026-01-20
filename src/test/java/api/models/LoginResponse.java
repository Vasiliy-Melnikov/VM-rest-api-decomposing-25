package api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {
    private String userId;

    @JsonProperty("userName")
    private String userName;

    private String token;
    private String expires;
    private String status;
    private String result;
}


