package com.ye.hospitalitops.controller;

import com.ye.hospitalitops.mapper.AssetMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest//问：为什么用 @WebMvcTest 不用 @SpringBootTest	答：前者只加载 Controller 和异常处理器，后者启动整个应用还连数据库，慢且依赖环境
public class AssetControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AssetMapper assetMapper;
    @Test
    void update_whenAssetNotFound_returns404() throws Exception{
        when(assetMapper.selectById(999L)).thenReturn(null);
        mockMvc.perform(put("/api/assets/999")
                            .contentType("application/json")
                            .content("""
                                {"name":"测试设备","category":"PC","dept":"信息科","criticalLevel":1}
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }
    @Test
    void add_whenNameBlank_returns400() throws Exception {
        mockMvc.perform(post("/api/assets")
                        .contentType("application/json")
                        .content("""
                            {"assetCode":"ZC-0001","name":"","category":"PC","dept":"信息科","criticalLevel":1}
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }
}
