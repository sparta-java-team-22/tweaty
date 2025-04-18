package com.tweaty.coupon.presentation.request;

import java.time.LocalDateTime;

public record CouponIssueRequest(LocalDateTime couponExpiryAt) {
}
