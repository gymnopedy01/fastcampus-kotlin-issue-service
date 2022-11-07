package com.fastcampus.userservice.controller

import com.fastcampus.userservice.model.*
import com.fastcampus.userservice.service.UserService
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import java.io.File

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
    suspend fun logout(@AuthToken token: String) {
        userService.logout(token)
    }

    //유저 정보를 가저오기
    @GetMapping("/me")
    suspend fun get(
        @AuthToken token: String
    ): MeResponse {
        return MeResponse(userService.getByToken(token))
    }

    //다른사용자가 유저명을 가저오기위한 API
    @GetMapping("/{userId}/username")
    suspend fun getUsername(@PathVariable userId: Long): Map<String, String> {
        return mapOf("reporter" to userService.get(userId).username)
    }

    //내정보 수정 API
    @PostMapping("/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun edit(
        @PathVariable id: Long,
        @ModelAttribute request: UserEditRequest,    //@RequestBody 가 아닌 @ModelAttribute사용한 이유는 컨텐트 타입이 applicaiton/json이 아닌 mulipart form data 이기 때문이다.
        @AuthToken token: String,
        @RequestPart("profile") filePart: FilePart, // FilePart 이미지나 파일형식을 전달받을수 있음.
    ) {
        //local에 이미지를 저장 /resouces/images폴더
        val orgFileName = filePart.filename()
        var filename: String? = null
        if (orgFileName.isNotEmpty()) {
            //abc.jpg   o.b.c.jpg
            val ext = orgFileName.substring(orgFileName.lastIndexOf(".") + 1)
            filename = "${id}.${ext}"       //1.jpg, 2.png      사용자id별 이미지

            //resource/images폴더를 구하기 위해 ClassPathResource 사용
            //resources/images/1.jpg
            val file = File(ClassPathResource("/images/").file, filename)
            filePart.transferTo(file).awaitSingleOrNull()           //Mono를 코루틴으로 변경해주는 awaitSingle을 사용
        }

        userService.edit(token, request.username, filename)

    }

}