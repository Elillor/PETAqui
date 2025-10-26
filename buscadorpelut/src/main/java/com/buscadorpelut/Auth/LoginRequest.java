package com.buscadorpelut.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @JsonProperty("email")
    private String emailUs;
    
    @JsonProperty("password")
    private String clauPas;

}
