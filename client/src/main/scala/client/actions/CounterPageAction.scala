package client.actions

import diode.Action

sealed trait CounterPageAction extends Action
object CounterPageAction {
  case object Increment extends CounterPageAction
  case object Decrement extends CounterPageAction
}
