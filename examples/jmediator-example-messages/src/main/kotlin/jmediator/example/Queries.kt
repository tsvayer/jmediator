package jmediator.example

import jmediator.Message
import jmediator.MessageMapping
import javax.validation.constraints.NotNull

@MessageMapping("example/query")
open class ExampleQuery(
  @NotNull var filter: String? = null
) : Message<ExampleQueryResponse>

open class ExampleQueryResponse(
  var result: String? = null
)
