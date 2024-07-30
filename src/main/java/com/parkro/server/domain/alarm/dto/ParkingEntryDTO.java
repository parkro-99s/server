package com.parkro.server.domain.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParkingEntryDTO {

    private String fcmToken;
    private String carNumber;
}
