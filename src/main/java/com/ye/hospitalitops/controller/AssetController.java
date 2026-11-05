package com.ye.hospitalitops.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ye.hospitalitops.entity.Asset;
import com.ye.hospitalitops.mapper.AssetMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController//返回的数据请直接变成 JSON
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetMapper assetMapper;
    //构造器注入，不是 @Autowired 字段注入。前者能被 final 修饰、方便写测试、循环依赖时启动就报错，后者藏问题。
    // 问“为什么”，答“便于单元测试和不可变”。↓
    public AssetController(AssetMapper assetMapper) {
        this.assetMapper = assetMapper;
    }

    @GetMapping
    //IPage 规定了返回值中必须有，current当前时期几页，size每页多少条，total共有多少条，records这一页的具体数据列表
    public IPage<Asset> list(@RequestParam(required = false) String dept,//所有传入的参数都是从浏览器网页发过来的
                             @RequestParam(required = false) String status,
                             @RequestParam(defaultValue = "1") long page,
                             @RequestParam(defaultValue = "10") long size) {
        Page<Asset> pageProgram = new Page<>(page,size);
        LambdaQueryWrapper<Asset> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dept!=null,Asset::getDept,dept);
        wrapper.eq(status != null, Asset::getStatus, status);
        return assetMapper.selectPage(pageProgram, wrapper);
    }
}