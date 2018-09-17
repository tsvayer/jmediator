package jmediator.example

import jmediator.Message
import jmediator.MessageMapping
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

@MessageMapping(
  "example/command",
  "example/commandAlias")
open class ExampleCommand(
  @NotNull var date: LocalDateTime? = null
) : Message<ExampleCommandResponse>

open class ExampleCommandResponse(
  var echoDate: LocalDateTime? = null
)

