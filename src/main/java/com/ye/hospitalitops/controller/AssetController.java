package com.ye.hospitalitops.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ye.hospitalitops.dto.AssetCreateRequest;
import com.ye.hospitalitops.entity.Asset;
import com.ye.hospitalitops.mapper.AssetMapper;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;


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
    //IPage 规定了返回值中必须有，current当前时期几页，size每页多少条，total共有多少条，records这一页的具体数据列表
    public  IPage<Asset> list(@RequestParam(required = false) String dept,
                              @RequestParam(required = false) String status,
                              @RequestParam(defaultValue = "1") long page,
                              @RequestParam(defaultValue ="10") long size)  {
        Page<Asset>pageGram=new Page<>(page,size);
        LambdaQueryWrapper<Asset> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(dept!=null,Asset::getDept,dept);
        wrapper.eq(status!=null,Asset::getStatus,status);
        return assetMapper.selectPage(pageGram,wrapper);

    }
    @PostMapping//RequestMapping是通用型如果要实现POST功能要写成（value="/add",method=REquestMethod.POST）
    public Long add(@RequestBody @Valid AssetCreateRequest req) {//@RequestBody：把请求体里的 JSON 反序列化成对象（Jackson);@Valid：触发 DTO 上那些 @NotBlank 之类的校验。
        Asset asset=new Asset();
        BeanUtils.copyProperties(req, asset);
        if(asset.getStatus()==null) asset.setStatus("RUNNING");
        assetMapper.insert(asset);
        return asset.getId();
    }
}