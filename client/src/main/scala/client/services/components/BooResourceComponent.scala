package client.services.components

import shared.dto.pickler.PicklerState._

import scala.concurrent.Future

trait BooResourceComponent {

  val booResource: BooResource

  trait BooResource {
    def get[A](url: String)(implicit u: Pickler[A]): Future[A]
    def post[A, B](url: String, request: A)(implicit a: Pickler[A], b: Pickler[B]): Future[B]
  }
}
