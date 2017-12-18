package client

import client.components.CSSTransitionGroup
import japgolly.scalajs.react.ReactDOM
import org.scalajs.dom

import scala.scalajs.js.JSApp
import scalacss.ScalaCssReact._
import scalacss.Defaults._
import scalacss.internal.mutable.GlobalRegistry

object App extends JSApp {
  val contentNode = dom.document.getElementById("root-container")

  def main(): Unit = {

    GlobalRegistry.addToDocumentOnRegistration()

    val cake = new ApplicationCake {}

    val _ = ReactDOM.render(cake.appRouter.router.withKey("router")(), contentNode)
    dom.console.log(CSSTransitionGroup)
  }
}
