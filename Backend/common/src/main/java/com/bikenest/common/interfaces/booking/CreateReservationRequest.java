package com.bikenest.common.interfaces.booking;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

//see /booking/add Endpoint
public class CreateReservationRequest {
    @Digits(integer=8,fraction = 0)
    private Integer bikenestId;

    @Digits(integer=8,fraction = 0)
    @DecimalMin("1.0")
    private Integer reservationMinutes;

    public Integer getBikenestId() {
        return bikenestId;
    }

    public void setBikenestId(Integer bikenestId) {
        this.bikenestId = bikenestId;
    }

    public Integer getReservationMinutes() {
        return reservationMinutes;
    }

    public void setReservationMinutes(Integer reservationMinutes) {
        this.reservationMinutes = reservationMinutes;
    }
}
