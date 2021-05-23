package com.example.matchreplace;

import com.example.matchreplace.vo.TreeNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/*
 * 主要包含两个三个Annotation
 * @EnableAutoConfiguration: @Enable一般用于借助 @Import 实现自动化导入自己需要的 @Configuration 类
 * 该类 import 了 EnableAutoConfigurationImportSelector，这个类可以利用 SpringFactoriesLoader 类智能的查找需要使用到的所有 @Configuration 类，然后利用java反射创建 @Configuration 配置类，加载到当前 IOC 容器中
 * SpringFactoriesLoader类，可以利用spring.factories文件（以 key = value 的形式存储类名与类名的一对多映射）找到需要的配置类全名
 *
 * @Configuration: 将应用程序本身作为一个配置类
 *
 * @ComponentScan: 扫描并加载自己创建的符合条件的Bean
 * */
@SpringBootApplication
@EnableEurekaClient
public class MatchReplaceApplication {


    public static void main(String[] args) {
        SpringApplication.run(MatchReplaceApplication.class, args);
        /*
         * 一个ApplicationContext提供:
         * • 访问应用程序组件的Bean工厂方法。从org.springframework.beans.factory.ListableBeanFactory继承。
         * • 以通用方式加载文件资源的能力。继承自org.springframe .core.io。ResourceLoader接口。---beanXML
         * • 向注册侦听器发布事件的能力。继承自ApplicationEventPublisher接口。
         * • 解析消息的能力，支持国际化。继承自MessageSource接口。
         * • 从父上下文继承。后代上下文中的定义总是优先级。例如，这意味着单个父上下文可以被整个web应用程序使用，而每个servlet都有自己独立于任何其他servlet的子上下文。
         * */
    }
    @LoadBalanced
    @Bean
    public RestTemplate rest() {

        return new RestTemplate();

    }

}
