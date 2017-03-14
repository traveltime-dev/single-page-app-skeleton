package client.pages

sealed trait Page
object Page {
  case object CounterPage extends Page
  case object AjaxPage    extends Page
}
