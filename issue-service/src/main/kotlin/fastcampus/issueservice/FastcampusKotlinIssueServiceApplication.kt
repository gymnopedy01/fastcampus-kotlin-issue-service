package fastcampus.issueservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class FastcampusKotlinIssueServiceApplication

fun main(args: Array<String>) {
    runApplication<FastcampusKotlinIssueServiceApplication>(*args)
}
