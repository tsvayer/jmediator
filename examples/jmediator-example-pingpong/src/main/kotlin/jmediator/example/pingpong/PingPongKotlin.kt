package jmediator.example.pingpong

import jmediator.toMono
import jmediator.Message
import jmediator.MessageHandlerRegistry
import jmediator.MessageMapping
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import java.time.LocalDateTime
import javax.annotation.PostConstruct
import javax.validation.constraints.NotNull

@MessageMapping("ping/send")
data class PingK(
        @NotNull val id: String? = null
) : Message<PongK>

data class PongK(
        val echoId: String,
        val timestamp: LocalDateTime = LocalDateTime.now()
)

@SpringBootApplication
@ComponentScan(basePackages = ["jmediator"])
open class PingPongApplicationK(
        private val registry: MessageHandlerRegistry
) {
    @PostConstruct
    fun routes() {
        registry.register(PingK::class.java) { (id) -> PongK(id.orEmpty()).toMono() }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(PingPongApplicationK::class.java, *args)
}
