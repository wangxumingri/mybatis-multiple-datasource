package com.example.demo.dao.crm;

import com.example.demo.entity.Columns;

public interface ColumnsMapper {
    int insert(Columns record);

    int insertSelective(Columns record);
}