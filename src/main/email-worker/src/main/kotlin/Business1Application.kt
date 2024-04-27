package su.arlet

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication

@SpringBootApplication
class Business1Application : SpringApplication() {

}

fun main(args: Array<String>) {
    runApplication<Business1Application>(*args)
}