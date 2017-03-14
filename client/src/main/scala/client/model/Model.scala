package client.model

import monocle.macros.Lenses

@Lenses
case class RootModel(
    counterPageModel: CounterPageModel = CounterPageModel(),
    ajaxPageModel: AjaxPageModel = AjaxPageModel()
)

object RootModel {
  val default = RootModel()
}

@Lenses
case class CounterPageModel(
    counter: Int = 0
)

@Lenses
case class AjaxPageModel(
    messages: Vector[String] = Vector.empty
)
