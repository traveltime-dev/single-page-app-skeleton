package client

import client.handlers.{AjaxHandlersComponent, CounterHandlerComponent}
import client.model._
import diode._
import diode.react.ReactConnector
import org.scalajs.dom

trait AppCircuitComponent {
  val appCircuit: AppCircuit

  trait AppCircuit extends Circuit[RootModel] with ReactConnector[RootModel]
}

trait ConcreteAppCircuitWired extends AppCircuitComponent {
  this: CounterHandlerComponent with AjaxHandlersComponent with InitialModelComponent =>

  override lazy val appCircuit       = new ConcreteAppCircuit
  private lazy val initialModelProxy = initialModel

  class ConcreteAppCircuit extends AppCircuit {

    val logActions = false

    class LoggingProcessor[M <: AnyRef] extends ActionProcessor[M] {
      def process(dispatch: Dispatcher, action: Any, next: Any => ActionResult[M], currentModel: M): ActionResult[M] = {
        dom.console.log(s"dispatching ${action.toString.take(80)}")
        next(action)
      }
    }

    if (logActions) {
      addProcessor(new LoggingProcessor[RootModel])
    }

    override def handleError(msg: String): Unit = {
      dom.console.error(s"handle error called with message $msg")
      super.handleError(msg)
    }

    override def handleFatal(action: Any, e: Throwable): Unit = {
      dom.console.error(
        s"handle fatal called with ${action.toString.take(80)} $e"
      )
      super.handleFatal(action, e)
    }

    override def actionHandler: HandlerFunction =
      foldHandlers(
        composeHandlers(
          counterHandlers.actionHandler,
          ajaxHandlers.actionHandler
        ),
        ajaxHandlers.eventHandler
      )

    override def initialModel: RootModel = initialModelProxy
  }

}
