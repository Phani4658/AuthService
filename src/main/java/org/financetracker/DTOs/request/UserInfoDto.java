package org.financetracker.DTOs.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.financetracker.entities.Users;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class UserInfoDto extends Users {
    private String username;

    private String lastname;

    private Long phoneNumber;

    private String email;
}
