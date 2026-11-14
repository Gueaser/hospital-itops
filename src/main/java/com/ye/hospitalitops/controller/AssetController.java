package com.ye.hospitalitops.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ye.hospitalitops.common.BizException;
import com.ye.hospitalitops.common.Result;
import com.ye.hospitalitops.dto.AssetCreateRequest;
import com.ye.hospitalitops.dto.AssetUpdateRequest;
import com.ye.hospitalitops.entity.Asset;
import com.ye.hospitalitops.mapper.AssetMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@Tag(name = "资产管理", description = "医院资产台账的查询、新增、修改与删除")
@RestController//负责告诉Spring这个方法返回的东西直接写进HTTP响应体。真正把 List<Asset>变成JSON的是Jackson（Spring Boot内置的MappingJackson2HttpMessageConverter）
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetMapper assetMapper;
    //构造器注入，不是 @Autowired 字段注入。前者能被 final 修饰、方便写测试、循环依赖时启动就报错，后者藏问题。
    // 问“为什么”，答“便于单元测试和不可变”。↓
    public AssetController(AssetMapper assetMapper) {
        this.assetMapper = assetMapper;
    }

    @GetMapping//同下
    @Operation(summary = "分页查询资产列表", description = "支持按科室(dept)和状态(status)进行动态筛选。如果不传page和size，默认返回第一页的10条数据。")
    //IPage 规定了返回值中必须有，current当前时期几页，size每页多少条，total共有多少条，records这一页的具体数据列表
    public  Result<IPage<Asset>> list(@Parameter(description = "所属科室")
                                          @RequestParam(required = false) String dept,
                                      @Parameter(description = "资产状态：RUNNING / DEGRADED / DOWN / SCRAPPED")
                                      @RequestParam(required = false) String status,
                                      @Parameter(description = "页码，从 1 开始")
                                      @RequestParam(defaultValue = "1")@Min(value = 1,message = "页数最小为1") long page,
                                      @Parameter(description = "每页条数，最大 100")
                                      @RequestParam(defaultValue ="10")@Max(value = 100,message = "每页最多 100 条")@Min(value = 1,message = "每页最少1条") long size)  {
        Page<Asset>pageGram=new Page<>(page,size);
        LambdaQueryWrapper<Asset> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(dept!=null,Asset::getDept,dept);
        wrapper.eq(status!=null,Asset::getStatus,status);
        return Result.ok(assetMapper.selectPage(pageGram,wrapper));

    }
    @PostMapping//RequestMapping是通用型如果要实现POST功能要写成（value="/add",method=REquestMethod.POST）
    @Operation(summary = "新增资产",description = "新增加一个资产，如没有传资产状态(status)则默认为“RUNNING”")
    public Result<Long> add(@RequestBody @Valid AssetCreateRequest req) {//@RequestBody：把请求体里的 JSON 反序列化成对象（Jackson);@Valid：触发 DTO 上那些 @NotBlank 之类的校验。
        Asset asset=new Asset();
        BeanUtils.copyProperties(req, asset);
        if(asset.getStatus()==null) asset.setStatus("RUNNING");
        assetMapper.insert(asset);
        return Result.ok(asset.getId());
    }
    @PutMapping("/{id}")
    @Operation(summary = "资产更新",description = "按 id 更新资产。请求体中未传的字段保持原值（部分更新语义）；资产不存在时返回 404。")
    public Result<Void> update(@Parameter(description = "资产 ID", example = "1")@PathVariable long id, @RequestBody @Valid AssetUpdateRequest req){
        Asset asset=assetMapper.selectById(id);
        if(asset==null){
            throw new BizException(404,"资产不存在");
        }
        BeanUtils.copyProperties(req, asset);
        assetMapper.updateById(asset);// 依赖 updateById 忽略 null 字段：未传的字段保持原值
        return Result.ok();
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "删除资产",description = "根据id物理删除对应资产，如果资产不存在，会返回 404 错误提示。")
    public Result<Void> delete(@Parameter(description = "资产 ID", example = "1")@PathVariable long id){
        int row=assetMapper.deleteById(id);
        if(row==0){
            throw  new BizException(404,"资产不存在");
        }
        return Result.ok();
    }
}