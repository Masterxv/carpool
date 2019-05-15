package com.campussay.carpool.ui.post;

import java.math.BigDecimal;

/**
 * create by WenJinG on 2019/4/20
 */
public class PostRequestBean {


    private BigDecimal destinationLatitude;
    private BigDecimal destinationLongitude;
    private BigDecimal originLatitude;
    private BigDecimal originLongitude;
    public void setOriginLongitude(BigDecimal originLongitude) {
        this.originLongitude = originLongitude;
    }

    public BigDecimal getOriginLongitude() {
        return originLongitude;
    }

    public void setOriginLatitude(BigDecimal originLatitude) {
        this.originLatitude = originLatitude;
    }

    public BigDecimal getOriginLatitude() {
        return originLatitude;
    }

    public void setDestinationLatitude(BigDecimal destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public BigDecimal getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLongitude(BigDecimal destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public BigDecimal getDestinationLongitude() {
        return destinationLongitude;
    }
}

