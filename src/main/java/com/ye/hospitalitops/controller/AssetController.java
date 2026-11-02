package com.ye.hospitalitops.controller;

import com.ye.hospitalitops.entity.Asset;
import com.ye.hospitalitops.mapper.AssetMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetMapper assetMapper;
    //构造器注入，不是 @Autowired 字段注入。前者能被 final 修饰、方便写测试、循环依赖时启动就报错，后者藏问题。
    // 问“为什么”，答“便于单元测试和不可变”。↓
    public AssetController(AssetMapper assetMapper) {
        this.assetMapper = assetMapper;
    }

    @GetMapping
    //返回值直接是 List<Asset>，没有 Result<T> 包装。这是 v0.1 的简化，等 D8 做统一响应体时再改。
    public List<Asset> list() {
        return assetMapper.selectList(null);// null 表示没有 where 条件。想加条件就传 QueryWrapper，第 5 周会用到
    }
}