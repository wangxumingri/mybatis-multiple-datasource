package com.example.demo.controller;

import com.example.demo.dao.crm.CrmColumnMapper;
import com.example.demo.dao.crm.CrmCouponTemplateMapper;
import com.example.demo.dao.mgt.MgtColumnMapper;
import com.example.demo.dao.mgt.MgtCouponTemplateMapper;
import com.example.demo.dto.BaseResponse;
import com.example.demo.entity.Column;
import com.example.demo.entity.CouponCode;
import com.example.demo.entity.CouponTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("copmpare")
public class CompareController {

    @Autowired
    private MgtCouponTemplateMapper mgtCouponTemplateMapper;

    @Autowired
    private CrmCouponTemplateMapper crmCouponTemplateMapper;

    @Autowired
    private CrmColumnMapper crmColumnMapper;

    @Autowired
    private MgtColumnMapper mgtColumnMapper;

    private static final String DB_CRM = "koala_crm";
    private static final String DB_MGT = "koala_crm2";
    // 表的主键
    private static Map<String, String> PRIMARY_KEY = new HashMap<>();
    // 需要过滤的字段
    private static List<String> filterColumn = new ArrayList<>();

    {
        filterColumn.add("gmt_create");

        PRIMARY_KEY.put("coupon_template", "id");
    }

    /**
     * 要比较的表也可以配置
     * @param tables
     * @return
     */
    @GetMapping
    public BaseResponse compareData(@RequestBody Set<String> tables) {
        Set<String> tableNames = new HashSet<>();
        tableNames.add("coupon_template");
        tableNames.add("coupon_code");
        // 1.获取2个数据库中表信息
        List<Column> crmTableColumns = crmColumnMapper.retrieveColumnList("koala_crm", tableNames);
        List<Column> mgt2TableColumns = mgtColumnMapper.retrieveColumnList("koala_crm2", tableNames);
        // 根据表对列进行分组
        Map<String, List<Column>> crmColumnMap = crmTableColumns.stream().collect(Collectors.groupingBy(Column::getTableName));
        Map<String, List<Column>> mgtTableColumnMap = mgt2TableColumns.stream().collect(Collectors.groupingBy(Column::getTableName));
        // Map<库,<表，列>>
        Map<String, Map<String, List<Column>>> tableInfoMap = new HashMap<>();
        tableInfoMap.put(DB_CRM, crmColumnMap);
        tableInfoMap.put(DB_MGT, mgtTableColumnMap);


        // 2.获取表数据
        Map<String, Class<?>> classMap = new HashMap<>();
        classMap.put("coupon_template", CouponTemplate.class);
        classMap.put("coupon_code", CouponCode.class);
        // 查询数据
        List<CouponTemplate> crmCouponTemplates = crmCouponTemplateMapper.selectAll();
        List<CouponTemplate> mgtCouponTemplates = mgtCouponTemplateMapper.selectAll();

        crmColumnMap.forEach((tableName, columns) -> {

        });

        List<CompareResult> results = new ArrayList<>();
        tables.forEach(tableName -> {
            try {
                CompareResult result = handler(null, crmCouponTemplates, mgtCouponTemplates, tableName);
                results.add(result);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        });

        return null;
    }


    /**
     * 直接从前往后比，还是根据主键转成Map后
     *
     * @param columns 一个表的列
     * @param crmData crm表数据
     * @param mgtData mgt表数据
     */
    private CompareResult handler(List<Column> columns, List<?> crmData, List<?> mgtData, String table) throws IllegalAccessException, NoSuchFieldException {
        assert crmData.size() == mgtData.size();
//        Class<?> aClass = crmData.get(0).getClass();
//        // 表的原始列
//        List<String> columnName = columns.stream()
//                .filter(column -> !column.getColumnName().equals("gmt_create"))// 排除列：也可通过配置排除
//                .map(Column::getColumnName)
//                .collect(Collectors.toList());
        // 获取表对应类的字段
//        ReflectionUtils.findField(aClass,)
        CompareResult result = new CompareResult();
        result.setTable(table);
        List<Content> contents = new ArrayList<>();
        result.setContents(contents);
        for (int i = 0; i < crmData.size(); i++) {
            Object mgtObject = mgtData.get(i);
            Class<?> aClass2 = mgtObject.getClass();
            Field[] declaredFields2 = aClass2.getDeclaredFields();
            Object crmObject = crmData.get(i);
            Class<?> aClass1 = crmObject.getClass();
            Field[] declaredFields1 = aClass1.getDeclaredFields();
            // 获取主键
            Field primaryKeyField = aClass2.getDeclaredField(PRIMARY_KEY.get("coupon_template"));
            primaryKeyField.setAccessible(true);
            Object primaryKey = primaryKeyField.get(mgtObject);
            // 组装不一致消息
            StringBuilder messageBuilder = new StringBuilder("");
            for (int i1 = 0; i1 < declaredFields1.length; i1++) {
                Field field1 = declaredFields1[i1];
                field1.setAccessible(true);
                Field field2 = declaredFields2[i1];
                field2.setAccessible(true);
                String propertyName = field1.getName();
                Object crmPropertyValue = field1.get(crmObject);
                Object mgtPropertyValue = field2.get(mgtObject);
                if (crmPropertyValue != null && mgtPropertyValue != null) {
                    // 都不为null
                    if (!crmPropertyValue.equals(mgtPropertyValue)) {
                        // 不一致：记录错误
                        messageBuilder.append("property:【")
                                .append(propertyName)
                                .append("】\tcrm:【")
                                .append(crmPropertyValue)
                                .append("】\tmgt:【")
                                .append(mgtPropertyValue)
                                .append("\n");
                    }
                } else if (crmPropertyValue == null && mgtPropertyValue == null) {
                    // 一致
                } else {
                    // 不一致：记录错误
                    messageBuilder.append("property:【")
                            .append(propertyName)
                            .append("】\tcrm:【")
                            .append(crmPropertyValue)
                            .append("】\tmgt:【")
                            .append(mgtPropertyValue)
                            .append("\n");
                }
            }

            if (messageBuilder.toString().length() > 0) {
                // 一个表中一行数据的校验结果
                Content content = new Content();
                content.setCrmOriginData(crmObject);
                content.setMgtOriginData(mgtObject);
                content.setMessage(messageBuilder.toString());
                content.setKey(primaryKey);
                contents.add(content);
            }
        }

        return result;
    }

    /**
     * 逐行报还是封装后一起报
     */

    public static class CompareResult {
        private String table;

        private List<Content> contents;


        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public List<Content> getContents() {
            return contents;
        }

        public void setContents(List<Content> contents) {
            this.contents = contents;
        }

        @Override
        public String toString() {
            return "CompareResult{" +
                    "table='" + table + '\'' +
                    ", contents=" + contents +
                    '}';
        }
    }

    public static class Content {
        /**
         * 主键
         */
        private Object key;
        /**
         * 不一致属性拼接
         */
        private String message;
        /**
         * crm原始数据
         */
        private Object crmOriginData;
        /**
         * mgt原始数据
         */
        private Object mgtOriginData;


        public Object getKey() {
            return key;
        }

        public void setKey(Object key) {
            this.key = key;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getCrmOriginData() {
            return crmOriginData;
        }

        public void setCrmOriginData(Object crmOriginData) {
            this.crmOriginData = crmOriginData;
        }

        public Object getMgtOriginData() {
            return mgtOriginData;
        }

        public void setMgtOriginData(Object mgtOriginData) {
            this.mgtOriginData = mgtOriginData;
        }

        @Override
        public String toString() {
            return "Content{" +
                    "key=" + key +
                    ", message='" + message + '\'' +
                    ", crmOriginData=" + crmOriginData +
                    ", mgtOriginData=" + mgtOriginData +
                    '}';
        }
    }


}
