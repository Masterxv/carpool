package com.campussay.carpool.ui.post;

import java.math.BigDecimal;

/**
 * create by WenJinG on 2019/4/20
 */
public class PostRouteData {
    private String originName;
    private String originAddressDetail;
    private BigDecimal originLongitude;
    private BigDecimal originLatitude;
    private String destinationName;
    private String  destinationAddressDetail;
    private BigDecimal destinationLongitude;
    private BigDecimal destinationLatitude;
    private Long targetTime;

    public void setDestinationAddressDetail(String destinationAddressDetail) {
        this.destinationAddressDetail = destinationAddressDetail;
    }

    public String getDestinationAdrssDetail() {
        return destinationAddressDetail;
    }

    public void setOriginAddressDetail(String originAdrssDetail) {
        this.originAddressDetail = originAdrssDetail;
    }

    public String getOriginAddressDetail() {
        return originAddressDetail;
    }

    public void setOriginLatitude(BigDecimal originLatitude) {
        this.originLatitude = originLatitude;
    }

    public BigDecimal getOriginLatitude() {
        return originLatitude;
    }

    public void setOriginLongitude(BigDecimal originLongitude) {
        this.originLongitude = originLongitude;
    }

    public BigDecimal getOriginLongitude() {
        return originLongitude;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getOriginName() {
        return originName;
    }

    public void setDestinationLatitude(BigDecimal destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public BigDecimal getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLongitude(BigDecimal detinationLongitude) {
        this.destinationLongitude = detinationLongitude;
    }

    public BigDecimal getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setTargetTime(Long targetTime) {
        this.targetTime = targetTime;
    }

    public Long getTargetTime() {
        return targetTime;
    }
}
