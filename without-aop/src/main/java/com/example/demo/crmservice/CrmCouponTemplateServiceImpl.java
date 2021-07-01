package com.example.demo.crmservice;

import com.example.demo.dao.crm.CrmCouponTemplateMapper;
import com.example.demo.dao.mgt.MgtCouponTemplateMapper;
import com.example.demo.entity.CouponTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CrmCouponTemplateServiceImpl implements CrmCouponTemplateService{

    @Autowired
    private CrmCouponTemplateMapper crmCouponTemplateMapper;

    @Autowired
    private MgtCouponTemplateMapper mgtCouponTemplateMapper;

    @Override
    @Transactional(transactionManager = "crmTransactionManager") // 指定该数据源自己的事务管理器，事务可以正常回滚
//    @Transactional // 未指定时，使用的是 @Primary 注解指定的 gmt 数据源的事务管理器，事务不会回滚
    public Long insert(CouponTemplate couponTemplate) {
        int crmEffectRows = crmCouponTemplateMapper.insert(couponTemplate);

        int i = 1/0;
        System.out.println(crmEffectRows);
        return crmEffectRows > 0 ? couponTemplate.getId() : -1;
    }
}
