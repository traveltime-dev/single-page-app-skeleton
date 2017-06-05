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
    addClassName("uk-container")
  )

  val button = style(
    addClassName("uk-button")
  )
  val primaryButton = style(
    addClassNames("uk-button", "uk-button-primary")
  )
}
