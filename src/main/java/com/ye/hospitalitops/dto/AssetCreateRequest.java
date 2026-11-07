package com.ye.hospitalitops.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.mybatis.spring.annotation.MapperScan;

import java.time.LocalDate;
import java.util.PrimitiveIterator;
@Data//测试时AssetCreateRequest没有接住JSON，报错400，所有字段全部为null，因为我少加了@Data，就没有set和get  (￣ω￣;)
public class AssetCreateRequest {
    @NotBlank(message = "资产编号不能为空")
    @Pattern(regexp = "ZC-\\d{4}$",message = "资产编号格式应为 ZC-0001")
    private String assetCode;

    @NotBlank(message = "设备名不能为空")
    @Size(max = 100,message = "设备名称不能超过 100 字")
    private String name;

    @NotBlank(message = "分类不能为空")
    private String category;

    @NotBlank(message = "科室不能为空")
    private String dept;

    private String loaction;

    //@NotBlank(message = "重要级别不能为空")不能用NotBlank 它只适配String
    @NotNull(message = "重要级别不能为空")
    @Min(value = 1,message = "重要级别只能是1/2/3")
    @Max(value = 3,message = "重要级别只能是1/2/3")
    private Integer criticalLevel;

    private String status;

    private String owner;

    private LocalDate lastMaintainedOn;

    private LocalDate nextMaintenanceOn;
}
