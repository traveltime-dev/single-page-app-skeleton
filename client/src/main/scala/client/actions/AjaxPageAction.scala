package client.actions

import diode.Action

trait AjaxPageAction extends Action
object AjaxPageAction {
  case object RequestMessage extends AjaxPageAction
}

trait AjaxPageEvent extends Action
object AjaxPageEvent {
  case class MessageReceived(message: String) extends AjaxPageAction
}