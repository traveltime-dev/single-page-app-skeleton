package client.handlers

import client.AppCircuitComponent
import client.actions.CounterPageAction
import client.model.CounterPageModel
import diode._
import scalaz._
import Scalaz._

trait CounterHandlerComponent {
  val counterHandlers: CounterHandlers

  trait CounterHandlers {
    val actionHandler: AppCircuitComponent#AppCircuit#HandlerFunction
  }

}
trait ConcreteCounterHandlersWired extends CounterHandlerComponent {
  this: CommonHandlerUtilsComponent with AppCircuitComponent =>

  override lazy val counterHandlers = new ConcreteCounterHandlersComponent

  class ConcreteCounterHandlersComponent extends CounterHandlers {

    val incrementDecrementHandler = new ActionHandler(
      commonHandlerUtils.counterModelZoom
    ) {
      override def handle = {
        case CounterPageAction.Increment =>
          updated(
            value |>
              CounterPageModel.counter.modify(_ + 1)
          )
        case CounterPageAction.Decrement =>
          updated(
            value |>
              CounterPageModel.counter.modify(_ - 1)
          )
      }
    }

    override val actionHandler = appCircuit.composeHandlers(incrementDecrementHandler)
  }
}
