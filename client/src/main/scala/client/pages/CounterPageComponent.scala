package client.pages

import client.actions.CounterPageAction
import client.components.AllTags._
import client.model.CounterPageModel
import client.styles.Styles.Styles
import diode.ActionType
import diode.react.ModelProxy
import japgolly.scalajs.react
import japgolly.scalajs.react.extra.ReusableFn
import japgolly.scalajs.react.{BackendScope, ReactComponentB, ReactComponentC, SyntheticEvent}
import org.scalajs.dom

import scalacss.Defaults._
import scalacss.ScalaCssReact._
import scalacss.internal.mutable.GlobalRegistry

trait CounterPageComponent {
  val counterPage: CounterPage

  trait CounterPage {
    case class Props(counterPageModel: CounterPageModel)

    def component: ReactComponentC.ReqProps[ModelProxy[Props], _, _, react.TopNode]

    def apply(props: ModelProxy[Props]) = component(props)
  }
}

trait ConcreteCounterPageWired extends CounterPageComponent {

  override lazy val counterPage = new ConcreteCounterPage

  class ConcreteCounterPage extends CounterPage {
    GlobalRegistry.register(new Style)
    val style = GlobalRegistry[Style].get

    override val component = ReactComponentB[ModelProxy[Props]]("CounterPage").stateless
      .renderBackend[Backend]
      .build

    class Backend($ : BackendScope[ModelProxy[Props], Unit]) {
      def dispatch[A: ActionType](a: A) =
        $.props.flatMap { p =>
          p.dispatchCB(a)
        }

      val onIncrement = ReusableFn((event: SyntheticEvent[dom.Node]) => dispatch(CounterPageAction.Increment))

      val onDecrement = ReusableFn((event: SyntheticEvent[dom.Node]) => dispatch(CounterPageAction.Decrement))

      def render(p: ModelProxy[Props], s: Unit) = {

        val c = p.wrap { identity }(p => <.div(p.value.counterPageModel.counter).render)

        <.div(
          Styles.bodyStyle,
          "current value",
          <.p(style.bold, c),
          <.flatButton(
            Styles.coloredBackgroundButton(Styles.redStyle),
            "Increment",
            ^.onClick ==> { e: SyntheticEvent[dom.Node] =>
              onIncrement(e)
            }
          ),
          <.flatButton(
            Styles.coloredBackgroundButton(Styles.blueStyle),
            "Decrement",
            ^.onClick ==> { e: SyntheticEvent[dom.Node] =>
              onDecrement(e)
            }
          )
        )
      }
    }

    class Style extends StyleSheet.Inline {
      import dsl._

      val bold = style(
        fontWeight._700
      )

    }
  }
}
