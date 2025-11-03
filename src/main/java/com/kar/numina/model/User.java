package com.kar.numina.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long userId;
    private String email;
    private String fullName;
    private String currencyPref;
    private String timezone;
    private Boolean twoFactorEnabled;
    private LocalDateTime createdAt;
}
