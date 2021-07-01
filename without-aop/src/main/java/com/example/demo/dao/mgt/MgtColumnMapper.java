package com.example.demo.dao.mgt;

import com.example.demo.entity.Column;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface MgtColumnMapper {
    List<Column> retrieveColumnList(@Param("tableSchema") String tableSchema, Set<String> tableName);
}
