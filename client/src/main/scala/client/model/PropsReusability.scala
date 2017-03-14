package client.model

import japgolly.scalajs.react.extra.Reusability._
import japgolly.scalajs.react.extra.Reusability

import scalacss.internal.StyleA

object PropsReusability {
  implicit val styleAReuse = Reusability.by((_: StyleA).htmlClass)
  implicit val doubleReuse = Reusability.double(0.001)
}