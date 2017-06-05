package client.pages

import client.AppCircuitComponent
import client.actions.AjaxPageAction
import client.components.AllTags._
import client.handlers.CommonHandlerUtilsComponent
import client.model.AjaxPageModel
import client.styles.Styles._
import diode.ActionType
import diode.react.ModelProxy
import japgolly.scalajs.react
import japgolly.scalajs.react.{BackendScope, ReactComponentB, ReactComponentC, SyntheticEvent}
import japgolly.scalajs.react.extra.ReusableFn
import org.scalajs.dom

import scalacss.Defaults._
import scalacss.ScalaCssReact._
import scalacss.internal.mutable.GlobalRegistry

trait AjaxPageComponent {
  val ajaxPage: AjaxPage

  trait AjaxPage {
    case class Props(ajaxPageModel: AjaxPageModel)

    def component: ReactComponentC.ReqProps[ModelProxy[Props], _, _, react.TopNode]

    def apply(props: ModelProxy[Props]) = component(props)
  }
}

trait ConcreteAjaxPageWired extends AjaxPageComponent { this: CommonHandlerUtilsComponent with AppCircuitComponent =>

  override lazy val ajaxPage = new ConcreteAjaxPageComponent

  class ConcreteAjaxPageComponent extends AjaxPage {
    GlobalRegistry.register(new Style)
    val style = GlobalRegistry[Style].get

    override val component = ReactComponentB[ModelProxy[Props]]("AjaxPage").stateless
      .renderBackend[Backend]
      .build

    class Backend($ : BackendScope[ModelProxy[Props], Unit]) {
      def dispatch[A: ActionType](a: A) =
        $.props.flatMap { p =>
          p.dispatchCB(a)
        }

      val onRequestMessage = ReusableFn((event: SyntheticEvent[dom.Node]) => dispatch(AjaxPageAction.RequestMessage))

      def render(p: ModelProxy[Props], s: Unit) = {

        val list = p.wrap { identity }(
          p => <.ul(style.messageList, p.value.ajaxPageModel.messages.map(m => <.li(m))).render
        )

        <.div(
          ^.className := "uk-padding-small",
          <.button(
            Styles.primaryButton,
            "Fetch from server",
            ^.onClick ==> { e: SyntheticEvent[dom.Node] =>
              onRequestMessage(e)
            }
          ),
          list
        )
      }
    }

    class Style extends StyleSheet.Inline {
      import dsl._

      val messageList = style(
        addClassNames("uk-list", "uk-list-bullet")
      )
    }
  }
}
