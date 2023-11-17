package com.nisum.challenge.usersms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phone {
    private String number;
    private String cityCode;
    private String countryCode;
}
