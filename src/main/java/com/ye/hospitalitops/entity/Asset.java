package com.ye.hospitalitops.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
//日期用的是 LocalDate / LocalDateTime，不是 java.util.Date。这是 Java 8 之后的标准做法。
// 问:“为什么不用 Date”答：Date 可变、时区处理混乱、月份从 0 开始。
@Data//Lombok 自动生成 getter/setter，不然 13 个字段要手写 26 个方法
@TableName("asset")//告诉它这个类对应 asset 表，不加就找不到表
public class Asset {

    @TableId(type = IdType.AUTO)//声明 id 是自增主键，这行漏了插入时 ID 会是 null
    private Long id;

    private String assetCode;
    private String name;
    private String category;
    private String dept;
    private String location;
    private Integer criticalLevel;
    private String status;
    private String owner;
    private LocalDate lastMaintainedOn;
    private LocalDate nextMaintenanceOn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
