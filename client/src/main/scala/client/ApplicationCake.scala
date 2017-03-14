// @formatter:off
package client

import client.handlers._
import client.pages._
import client.services._

trait ApplicationCake
  extends ConcreteInitialModelWired

  with ConcreteAppCircuitWired
  with ConcreteAppRouterWired

  with ConcreteAjaxPageWired
  with ConcreteAjaxHandlersWired
  with ConcreteMessageServiceWired

  with ConcreteCounterHandlersWired
  with ConcreteCounterPageWired

  with ConcreteCommonHandlerUtilsWired

  with AjaxBooResourceWired