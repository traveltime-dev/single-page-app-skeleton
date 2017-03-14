package client.services

import shared.dto.pickler.PicklerState._
import client.services.components.BooResourceComponent
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.ext.Ajax.InputData

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js.typedarray.{ArrayBuffer, TypedArrayBuffer}

trait AjaxBooResourceWired extends BooResourceComponent {

  override lazy val booResource: BooResource = new AjaxService

  class AjaxService extends BooResource {
    def get[A](url: String)(implicit u: Pickler[A]): Future[A] = {
      Ajax
        .get(url, responseType = "arraybuffer")
        .map { r =>
          val arrayBuffer = r.response.asInstanceOf[ArrayBuffer]
          val typedBuffer = TypedArrayBuffer.wrap(arrayBuffer)
          Unpickle[A].fromBytes(typedBuffer)
        }
    }

    def post[A, B](url: String, request: A)(implicit a: Pickler[A], b: Pickler[B]): Future[B] = {
      Ajax
        .post(
          url,
          responseType = "arraybuffer",
          headers = Map("Content-Type" -> "application/octet-stream"),
          data = InputData.byteBuffer2ajax(Pickle.intoBytes(request))
        )
        .map { r =>
          val arrayBuffer = r.response.asInstanceOf[ArrayBuffer]
          val typedBuffer = TypedArrayBuffer.wrap(arrayBuffer)
          Unpickle[B].fromBytes(typedBuffer)
        }
    }
  }
}
