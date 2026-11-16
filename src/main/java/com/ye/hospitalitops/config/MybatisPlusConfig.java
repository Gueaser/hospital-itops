//MyBatis-Plus 分页插件，物理分页，SQL 层面 LIMIT，实现“真分页”，也就是物理分页，数据库用limit只查10，网络只传十条
//不配这个selectPage 会把全表 20 条都查出来，然后在内存里截取前 10 条，这个是“假分页”，也就是逻辑分页，全部查出来在截取10条
package com.ye.hospitalitops.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.ye.hospitalitops.mapper")//测试时将MapperScan转移到这里，因为测试类启动时扫描到这个就会强制连接数据库，但是测试类没配置数据类，
                                            // 还放在原本的HospitalItopsApplication.java下面的话就会报错，，放到这里@Configuration下面的在轻量级测试中不加载
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
