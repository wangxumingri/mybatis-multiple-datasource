package com.example.demo.service.impl;

import com.example.demo.crmservice.CrmCouponTemplateService;
import com.example.demo.dao.crm.CrmColumnMapper;
import com.example.demo.dao.mgt.MgtCouponTemplateMapper;
import com.example.demo.dao.crm.CrmCouponTemplateMapper;
import com.example.demo.entity.CouponTemplate;
import com.example.demo.service.CouponTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CouponTemplateServiceImpl implements CouponTemplateService {

    @Autowired
    private CrmCouponTemplateService crmCouponTemplateService;

    @Autowired
    private MgtCouponTemplateMapper mgtCouponTemplateMapper;

    @Autowired
    private CrmColumnMapper crmColumnMapper;

    @Autowired
    private CrmCouponTemplateMapper crmCouponTemplateMapper;


    @Override
    @Transactional
    public Long insert(CouponTemplate couponTemplate) {

        int mgtEffectRows = mgtCouponTemplateMapper.insert(couponTemplate);
        // 在其他数据源的事务中，直接调用其他数据源的mapper ，其他数据源的事务也不会回滚，可以包装成service调用
        int crmEffectRows = crmCouponTemplateMapper.insert(couponTemplate);
//        Long mgtEffectRows = crmCouponTemplateService.insert(couponTemplate);
        System.out.println(crmEffectRows);
        System.out.println(mgtEffectRows);


        return crmEffectRows > 0 ? couponTemplate.getId():null;
    }
    @Override
    public int updateByPrimaryKey(CouponTemplate couponTemplate) {
        int i = mgtCouponTemplateMapper.updateByPrimaryKey(couponTemplate);
        return i;
    }
    @Override
    public CouponTemplate selectByPrimaryKey(Long id) {
        return mgtCouponTemplateMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<CouponTemplate> selectAll() {
        return mgtCouponTemplateMapper.selectAll();
    }
}
