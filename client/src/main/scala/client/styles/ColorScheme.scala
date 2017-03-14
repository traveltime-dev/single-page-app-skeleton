package client.styles

import scalacss.internal.Macros
import japgolly.univeq.UnivEq

case class ColorScheme(
    mainColor: Macros.Color,
    focusShade: Macros.Color,
    activeShade: Macros.Color,
    value: String
)

object ColorScheme {
  implicit def colorEq: UnivEq[Macros.Color] = UnivEq.derive
  implicit def shapeEq: UnivEq[ColorScheme]  = UnivEq.derive
}
