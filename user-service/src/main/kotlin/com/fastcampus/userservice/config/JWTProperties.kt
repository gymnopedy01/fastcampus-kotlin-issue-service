package com.fastcampus.userservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding


//불변 형태를 유지할수있ㄷ록 constructor binding 추가
//@ConstructorBinding 설정 클래스의 생성자의 값이 추가되게됨
@ConstructorBinding
@ConfigurationProperties(prefix="jwt")
data class JWTProperties (
    val issuer:String,
    val subject:String,
    val expiresTime:Long,
    val secret:String,
)
