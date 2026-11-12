package com.ye.hospitalitops.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AssetUpdateRequest {
    @NotBlank(message = "设备名不能为空")
    @Size(max = 100,message = "设备名称不能超过 100 字")
    private String name;

    @NotBlank(message = "分类不能为空")
    private String category;

    @NotBlank(message = "科室不能为空")
    private String dept;

    private String location;

    @NotNull(message = "重要级别不能为空")
    @Min(value = 1,message = "重要级别只能是1/2/3")
    @Max(value = 3,message = "重要级别只能是1/2/3")
    private Integer criticalLevel;

    @Pattern(regexp="RUNNING|DEGRADED|DOWN|SCRAPPED",message = "status状态只能为RUNNING/DEGRADED/DOWN/SCRAPPED其一")
    private String status;

    private String owner;

    private LocalDate lastMaintainedOn;

    private LocalDate nextMaintenanceOn;
}
