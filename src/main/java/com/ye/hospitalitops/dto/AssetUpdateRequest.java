package com.ye.hospitalitops.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AssetUpdateRequest {
    @NotBlank(message = "设备名不能为空")
    @Size(max = 100,message = "设备名称不能超过 100 字")
    @Schema(description = "设备名称", example = "门诊收费电脑")
    private String name;

    @NotBlank(message = "分类不能为空")
    @Schema(description = "资产分类", example = "PC")
    private String category;

    @NotBlank(message = "科室不能为空")
    @Schema(description = "所属科室", example = "信息科")
    private String dept;

    @Schema(description = "所在位置", example = "门诊大厅")
    private String location;

    //@NotBlank(message = "重要级别不能为空")不能用NotBlank 它只适配String
    @NotNull(message = "重要级别不能为空")
    @Min(value = 1,message = "重要级别只能是1/2/3")
    @Max(value = 3,message = "重要级别只能是1/2/3")
    @Schema(description = "重要级别：1高 2中 3低", example = "1")
    private Integer criticalLevel;

    @Pattern(regexp="RUNNING|DEGRADED|DOWN|SCRAPPED",message = "status状态只能为RUNNING/DEGRADED/DOWN/SCRAPPED其一")
    @Schema(description = "资产状态:RUNNING/DEGRADED/DOWN/SCRAPPED", example = "RUNNING")
    private String status;

    @Schema(description = "资产所有者", example = "张三")
    private String owner;

    @Schema(description = "上次维修日期", example = "yyyy-MM-dd")
    private LocalDate lastMaintainedOn;

    @Schema(description = "下次维修日期", example = "yyyy-MM-dd")
    private LocalDate nextMaintenanceOn;
}
