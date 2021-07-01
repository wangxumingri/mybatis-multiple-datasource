package com.example.demo.dao.crm;

import com.example.demo.entity.CouponTemplate;

import java.util.List;

public interface CrmCouponTemplateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponTemplate record);

    int insertSelective(CouponTemplate record);

    CouponTemplate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponTemplate record);

    int updateByPrimaryKey(CouponTemplate record);

    List<CouponTemplate> selectAll();
}