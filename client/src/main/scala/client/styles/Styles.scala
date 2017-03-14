package client.styles

import scala.language.postfixOps
import scalacss.internal.mutable.GlobalRegistry
import scalacss.Defaults._

object Styles {
  GlobalRegistry.register(new Styles())

  lazy val Styles = GlobalRegistry[Styles].get
}

class Styles extends StyleSheet.Inline {
  import dsl._

  val bodyStyle = style(
    padding.`0`,
    margin.`0`,
    height(100 %%),
    width(100 %%)
  )

  val noselect = style(
    userSelect := "none"
  )

  val materialIcon = noselect + style(
    addClassName("material-icons")
  )

  val mainBackgroundColor = c"#ffffff"

  val grayBorder        = c"#7a7a7b"
  val backgroundHovered = c"rgba(0, 0, 0, 0.12)"
  val backgroundClicked = c"rgba(153, 153, 153, 0.4)"

  val defaultBackgroundColorTransition = style(
    transition := "background-color 0.25s"
  )

  val flatButton = noselect + defaultBackgroundColorTransition + style(
    backgroundColor(mainBackgroundColor),
    border(2 px, solid, mainBackgroundColor),
    height(40 px),
    borderRadius(22 px),
    textAlign.center,
    verticalAlign.middle,
    lineHeight(40 px),
    display.inlineBlock,
    padding(0 px, 24 px),
    fontSize(15 px),
    fontWeight._400,
    color.black,
    cursor.pointer,
    &.hover(
      backgroundColor(backgroundHovered)
    ),
    &.active(
      backgroundColor(backgroundClicked)
    )
  )

  val redStyle = ColorScheme(
    mainColor = c"#d51058",
    focusShade = c"#bc0e4e",
    activeShade = c"#881a4b",
    value = "#d51058"
  )

  val blueStyle = ColorScheme(
    mainColor = c"#39d2e2",
    focusShade = c"#32b9c8",
    activeShade = c"#2a8e9e",
    value = "#39d2e2"
  )

  val colorDomain = Domain.ofValues(
    redStyle,
    blueStyle
  )

  val coloredBackgroundButton = styleF(colorDomain)(
    c =>
      styleS(
        backgroundColor(c.mainColor),
        &.hover(
          backgroundColor(c.focusShade)
        ),
        &.active(
          backgroundColor(c.activeShade)
        ),
        transition := "background 0.25s"
    )
  )
}
