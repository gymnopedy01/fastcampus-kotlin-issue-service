package com.fastcampus.userservice.controller

import com.fastcampus.userservice.model.*
import com.fastcampus.userservice.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/signup")
    suspend fun signUp(@RequestBody request: SignUpRequest) {
        userService.signUp(request)
    }

    @PostMapping("/signin")
    suspend fun signIn(@RequestBody singnInRequest: SignInRequest): SignInResponse {
        return userService.signIn(singnInRequest)
    }

    //컨트롤러에서 token을 받아
    //Token 객체를 파싱할수있는 Handler ArgumentResolver 사용하여 @authtoken 어노테이셩인경우 handler arguentresolver에서
    @DeleteMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun logout(@AuthToken token : String) {
        userService.logout(token)
    }

    //유저 정보를 가저오기
    @GetMapping("/me")
    suspend fun get(
        @AuthToken token: String
    ) : MeResponse {
        return MeResponse(userService.getByToken(token))
    }
}