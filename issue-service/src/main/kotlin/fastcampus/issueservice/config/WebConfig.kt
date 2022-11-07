package fastcampus.issueservice.config

import com.fasterxml.jackson.annotation.JsonProperty
import fastcampus.issueservice.exception.UnAuthorizedException
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
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

    //정적 리소스 접근하려면 AddResourceHandlers 설정을 해줘야함
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/**")
            .addResourceLocations(*arrayOf(
                "classpath:/META-INF/resources",
                "classpath:/resources/",
                "classpath:/static/",
                "classpath:/public/",
            ))
    }
}


// 컨트롤러에 인자로 들어오는 특정 객체 에노테이션에 대해서
// 서포츠 파라미터에 해당하는 파라미터에 해당 조건이 맞는다면
// 리졸브 아규먼트를 통해서 해당 어노테이션 또는 객체에대해서 세팅 또는 조작을 할수있는 인터페이스
@Component
class AuthUserHandlerArgumentResolver(
    @Value("\${auth.url}") val authUrl: String,
    ) : HandlerMethodArgumentResolver {

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
//        return AuthUser(
//            userId = 1,
//            username = " 테스트 ",
//        )

        val authHeader = webRequest.getHeader("Authorization") ?: throw UnAuthorizedException()

        // RestTemplate 대신 WebClient를 사용
        return runBlocking {
            WebClient.create()
                .get()
                .uri(authUrl)
                .header("Authorization", authHeader)        //Bearer eXydjxkck.sdf.asdf.
                .retrieve()
                .awaitBody<AuthUser>()      // bodytoMono 사용해도되지만 코루틴 기반의 awaitBody 확장함수 도 제공
                                            //일반 함수이기때문에 awaitBody 시 suspend가 필요, runBlocking 으로 감싼다.
        }

    }

}

data class AuthUser(
    @JsonProperty("id")
    val userId: Long,
    val username: String,
    val email:String,
    val profileUrl: String? = null
)