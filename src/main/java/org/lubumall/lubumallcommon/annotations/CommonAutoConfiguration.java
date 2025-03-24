package org.lubumall.lubumallcommon.annotations;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AutoConfigureAfter(WebMvcConfigurer.class)
public class CommonAutoConfiguration implements WebMvcConfigurer {

  @Bean
  @ConditionalOnMissingBean(RoleInterceptor.class)
  public RoleInterceptor roleInterceptor() {
    return new RoleInterceptor();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(roleInterceptor());
  }
}
