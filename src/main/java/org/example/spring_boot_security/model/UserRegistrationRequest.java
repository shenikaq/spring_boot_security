package org.example.spring_boot_security.model;

import lombok.Data;

@Data
public class UserRegistrationRequest {

    String userName;
    String email;
    String password;

}
