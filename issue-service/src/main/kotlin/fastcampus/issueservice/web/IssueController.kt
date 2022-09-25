package fastcampus.issueservice.web

import fastcampus.issueservice.config.AuthUser
import fastcampus.issueservice.model.IssueRequest
import fastcampus.issueservice.service.IssueService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/issues")
class IssueController(
    private val issueService: IssueService
) {
    
    //핸들러 메소드 아규먼트 리졸버에 의해서 authUser를 받을수 있게됨

    @PostMapping
    fun create(
        authUser: AuthUser,
        @RequestBody request: IssueRequest,
    ) = issueService.create(authUser.userId, request)

}