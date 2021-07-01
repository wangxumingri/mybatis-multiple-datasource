package com.example.demo.dao.crm;

import com.example.demo.entity.Column;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface CrmColumnMapper {
    List<Column> retrieveColumnList(@Param("tableSchema") String tableSchema, Set<String> tableName);
}
