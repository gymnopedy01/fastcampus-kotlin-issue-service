package com.fastcampus.userservice.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class WebConfig : WebFluxConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET","POST","PUT","DELETE")
            .maxAge(3600)
        //웹브라우저에서 크로스오리진으로 호출했을때
        //먼저 프리플라이트 라는 동작을 통해서 OPTIONS 메서드로먼저 호출하게 되고
        // 거기에 따른 allow 관련 응답헤더를 받아서, 실제 호출 동작을 하게된다.

    }
}