//package com.example.demo;
//
//import java.util.Properties;
//
//import javax.sql.DataSource;
//
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
//import com.github.pagehelper.PageInterceptor;
//
//@Configuration
//@MapperScan("com.example.demo.mapper") // 你的 Mapper 包路径
//public class MyBatisConfig {
//
//    @Bean
//    public PageInterceptor pageInterceptor() {
//        PageInterceptor pageInterceptor = new PageInterceptor();
//        Properties properties = new Properties();
//        // 配置参数，按需设置
//        properties.setProperty("helperDialect", "mysql");
//        properties.setProperty("reasonable", "true");
//        pageInterceptor.setProperties(properties);
//        return pageInterceptor;
//    }
//
//    @Bean
//    public MybatisSqlSessionFactoryBean sqlSessionFactory(DataSource dataSource, PageInterceptor pageInterceptor) {
//        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
//        factoryBean.setDataSource(dataSource);
//        factoryBean.setPlugins(pageInterceptor); // 设置分页插件
//        return factoryBean;
//    }
//}
//
