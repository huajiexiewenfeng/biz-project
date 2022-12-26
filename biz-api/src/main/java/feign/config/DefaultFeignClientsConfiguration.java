//package feign.config;
//
//import org.springframework.cloud.openfeign.FeignClientsConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Import;
//
///**
// * 默认 FeignClients 配置（扩展 Spring Cloud FeignClientsConfiguration）
// *
// * @Author: xiewenfeng
// * @Date: 2022/12/26 9:59
// */
//@Import(value = {FeignClientsConfiguration.class})
//public class DefaultFeignClientsConfiguration {
//    @Bean
//    public FeignCallCounterMetrics feignCallCounterMetrics() {
//        return new FeignCallCounterMetrics();
//    }
//
//}
