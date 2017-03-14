package client.handlers

import client.AppCircuitComponent
import client.model.{AjaxPageModel, CounterPageModel, RootModel}
import diode.ModelRW

trait CommonHandlerUtilsComponent {
  val commonHandlerUtils: CommonHandlerUtils

  trait CommonHandlerUtils {
    val counterModelZoom: ModelRW[RootModel, CounterPageModel]
    val ajaxModelZoom: ModelRW[RootModel, AjaxPageModel]
  }
}

trait ConcreteCommonHandlerUtilsWired extends CommonHandlerUtilsComponent { this: AppCircuitComponent =>

  override lazy val commonHandlerUtils = new ConcreteCommonHandlerUtils

  class ConcreteCommonHandlerUtils extends CommonHandlerUtils {
    override val counterModelZoom = appCircuit.zoomRW(_.counterPageModel)((m, v) => m.copy(counterPageModel = v))
    override val ajaxModelZoom    = appCircuit.zoomRW(_.ajaxPageModel)((m, v) => m.copy(ajaxPageModel = v))

  }
}
