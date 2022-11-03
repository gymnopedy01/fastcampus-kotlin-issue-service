package com.fastcampus.userservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

//@ConfigurationProperties 가 등록된 클래스들을 찾아 값들을 주입하고 빈으로 등록해준다. https://mangkyu.tistory.com/191 [MangKyu's Diary:티스토리]
@SpringBootApplication
@ConfigurationPropertiesScan
class FastcampusUserServiceApplication

fun main(args:Array<String>) {
    runApplication<FastcampusUserServiceApplication>()
}
