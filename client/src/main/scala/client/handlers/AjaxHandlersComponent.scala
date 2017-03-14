package client.handlers

import client.AppCircuitComponent
import client.actions.{AjaxPageAction, AjaxPageEvent}
import client.model.AjaxPageModel
import client.services.MessageServiceComponent
import diode.{ActionHandler, Effect}
import scalaz._
import Scalaz._
import scala.concurrent.ExecutionContext.Implicits.global

trait AjaxHandlersComponent {
  val ajaxHandlers: AjaxHandlers

  trait AjaxHandlers {
    val actionHandler: AppCircuitComponent#AppCircuit#HandlerFunction
    val eventHandler: AppCircuitComponent#AppCircuit#HandlerFunction
  }

}
trait ConcreteAjaxHandlersWired extends AjaxHandlersComponent {
  this: CommonHandlerUtilsComponent with MessageServiceComponent with AppCircuitComponent =>

  override lazy val ajaxHandlers = new ConcreteAjaxHandlersComponent

  class ConcreteAjaxHandlersComponent extends AjaxHandlers {

    val ajaxActionHandler = new ActionHandler(
      commonHandlerUtils.ajaxModelZoom
    ) {
      override def handle = {
        case AjaxPageAction.RequestMessage =>
          val future = messageService.getMessage().map(m => AjaxPageEvent.MessageReceived(m.isoDate))
          effectOnly(Effect(future))
      }
    }

    val ajaxEventHandler = new ActionHandler(
      commonHandlerUtils.ajaxModelZoom
    ) {
      override def handle = {
        case AjaxPageEvent.MessageReceived(message) =>
          updated(
            value |> AjaxPageModel.messages.modify(_ :+ message)
          )
      }
    }

    override val actionHandler = appCircuit.composeHandlers(ajaxActionHandler)

    override val eventHandler = appCircuit.foldHandlers(ajaxEventHandler)
  }
}
