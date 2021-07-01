package com.example.demo.crmservice;

import com.example.demo.entity.CouponTemplate;

public interface CrmCouponTemplateService {
    /**
     * 新增券模版
     * @param couponTemplate
     * @return
     */
    Long insert(CouponTemplate couponTemplate);
}
