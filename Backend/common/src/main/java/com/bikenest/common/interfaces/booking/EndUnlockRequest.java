package com.bikenest.common.interfaces.booking;

public class EndUnlockRequest {
    private Integer bookingId;
    private String qrCode;

    public EndUnlockRequest(){}

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }
}
