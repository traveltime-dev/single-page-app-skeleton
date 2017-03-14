package client

import client.model.RootModel

trait InitialModelComponent {
  val initialModel: RootModel
}

trait ConcreteInitialModelWired extends InitialModelComponent {
  override lazy val initialModel = RootModel.default
}