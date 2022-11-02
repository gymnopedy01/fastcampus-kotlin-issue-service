package com.fastcampus.userservice.config

import com.fastcampus.userservice.model.AuthToken
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Configuration
class WebConfig(
    private val authTokenResolver: AuthTokenResolver
) : WebFluxConfigurer {

    override fun configureArgumentResolvers(configurer: ArgumentResolverConfigurer) {
        super.configureArgumentResolvers(configurer)
        configurer.addCustomResolver(authTokenResolver)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .maxAge(3600)
        //웹브라우저에서 크로스오리진으로 호출했을때
        //먼저 프리플라이트 라는 동작을 통해서 OPTIONS 메서드로먼저 호출하게 되고
        // 거기에 따른 allow 관련 응답헤더를 받아서, 실제 호출 동작을 하게된다.

    }


}


//supportsParameter가 true인 경우 resolveArgument가 동작하게됨
@Component
class AuthTokenResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(AuthToken::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        bindingContext: BindingContext,
        exchange: ServerWebExchange
    ): Mono<Any> {

        //요청해더에 "Authorization 헤더가 존재하고
        val authHeader = exchange.request.headers["Authorization"]?.first()
        checkNotNull(authHeader)        //authHeader 가 null 이면 Exception을 발생 시키기 위함

        val token =
            authHeader.split(" ")[1]        // Authorization : Bearer sfasdfa 이기때문에 [0]은 Bearer, [1]은 토큰 이기때문에 토큰만 가져오기 위함
        return token.toMono()
    }

}