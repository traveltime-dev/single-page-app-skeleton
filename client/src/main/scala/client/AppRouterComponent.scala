package client

import client.components.AllTags._
import client.pages._
import client.styles.Styles._
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.ReactAttr

import scalacss.ScalaCssReact._

trait AppRouterComponent {
  val appRouter: AppRouter

  trait AppRouter
}

trait ConcreteAppRouterWired extends AppRouterComponent {
  this: AppCircuitComponent with CounterPageComponent with AjaxPageComponent =>

  override lazy val appRouter = new ConcreteAppRouter

  class ConcreteAppRouter extends AppRouter {
    val counterPageConnector = appCircuit.connect(m => counterPage.Props(m.counterPageModel))
    val ajaxPageConnector    = appCircuit.connect(m => ajaxPage.Props(m.ajaxPageModel))

    val baseUrl = BaseUrl.fromWindowOrigin_/ / "#"

    val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
      import dsl._

      (
        emptyRule
          | staticRoute(root / "counter", Page.CounterPage) ~> render(counterPageConnector(counterPage(_)))
          | staticRoute(root / "ajax", Page.AjaxPage) ~> render(ajaxPageConnector(ajaxPage(_)))
      ).notFound(redirectToPage(Page.CounterPage)(Redirect.Replace))
        .renderWith(layout)
        .verify(Page.CounterPage, Page.AjaxPage)
    }

    val router = Router(baseUrl, routerConfig)

    def layout(router: RouterCtl[Page], r: Resolution[Page]) = {
      <.div(
        Styles.bodyStyle,
        <.ul(
          ^.className := "uk-tab",
          ReactAttr.Generic("data-uk-tab") := "",
          <.li( (r.page == Page.CounterPage) ?= (^.className := "uk-active"), <.a("Counter page", router.setOnLinkClick(Page.CounterPage))),
          <.li((r.page == Page.AjaxPage) ?= (^.className := "uk-active"),  <.a("Ajax page", router.setOnLinkClick(Page.AjaxPage)))
        ),
        r.render()
      )
    }
  }
}
