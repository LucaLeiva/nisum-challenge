package com.nisum.challenge.usersms.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PhoneEmbedded {
    private String number;
    private String cityCode;
    private String countryCode;
}
