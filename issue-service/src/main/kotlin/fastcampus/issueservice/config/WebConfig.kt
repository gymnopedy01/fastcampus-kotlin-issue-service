package fastcampus.issueservice.config

import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport

@Configuration
class WebConfig(
    private val authUserHandlerMethodArgumentResolver: AuthUserHandlerArgumentResolver
) : WebMvcConfigurationSupport() {

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {

        // apply는 코틀린의 스코프 함수
        // 리스트의 애드를 통해 추가
        argumentResolvers.apply {
            add(authUserHandlerMethodArgumentResolver)
        }
    }
}


// 컨트롤러에 인자로 들어오는 특정 객체 에노테이션에 대해서
// 서포츠 파라미터에 해당하는 파라미터에 해당 조건이 맞는다면
// 리졸브 아규먼트를 통해서 해당 어노테이션 또는 객체에대해서 세팅 또는 조작을 할수있는 인터페이스
@Component
class AuthUserHandlerArgumentResolver : HandlerMethodArgumentResolver {

    //파라미터로 들어온 AuthUser 와 AuthUserClass타입이 동일하다면 이조건은 만족하여
    //하위에 있는 resolveArgument가 동작함
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        AuthUser::class.java.isAssignableFrom(parameter.parameterType)

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        //더미 유저 객체
        return AuthUser(
            userId = 1,
            username = " 테스트 ",
        )
    }

}

data class AuthUser(
    val userId: Long,
    val username: String,
    val profileUrl: String? = null
)