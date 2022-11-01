package com.fastcampus.userservice.service

import com.fastcampus.userservice.domain.entity.User
import com.fastcampus.userservice.domain.repository.UserRepository
import com.fastcampus.userservice.exception.UserExistsException
import com.fastcampus.userservice.model.SignUpRequest
import com.fastcampus.userservice.utils.BCryptUtils
import org.springframework.stereotype.Service

@Service
class UserService (
    private val userRepository: UserRepository,
) {
    suspend fun signUp(signUpRequest: SignUpRequest) {
        with(signUpRequest) {

            //email일 조회했는데 만약 존재한다면 중복이므로
            userRepository.findByEmail(email)?.let{
                throw UserExistsException()
            }
            val user = User(
                email = email,
                password = BCryptUtils.hash(password),
                username = username,
            )

        }

    }
}