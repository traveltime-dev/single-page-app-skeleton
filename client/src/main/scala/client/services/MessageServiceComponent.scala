package client.services

import scala.concurrent.Future
import shared.dto
import client.services.components.BooResourceComponent

trait MessageServiceComponent {
  val messageService: MessageService

  trait MessageService {
    def getMessage(): Future[dto.responses.DateResponse]
  }
}

trait ConcreteMessageServiceWired extends MessageServiceComponent { this: BooResourceComponent =>
  override lazy val messageService = new ConcreteMessageService

  class ConcreteMessageService extends MessageService {
    override def getMessage(): Future[dto.responses.DateResponse] =
      booResource.get[dto.responses.DateResponse]("/api/date")
  }
}
