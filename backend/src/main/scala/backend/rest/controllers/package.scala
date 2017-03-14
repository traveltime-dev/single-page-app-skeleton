package backend.rest

import shared.dto.pickler.PicklerState._
import play.api.Logger
import play.api.mvc._

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

package object controllers extends backend.rest.mappers.todto.Implicits with backend.rest.mappers.tomodel.Implicits {
  implicit val defaultPlayExecutionContext = play.api.libs.concurrent.Execution.defaultContext
  private val logger                       = Logger(getClass)

  def postAction[A, B](action: (A => Future[B]))(implicit a: Pickler[A], b: Pickler[B]) = Action.async { request =>
    val requestBody = extractBody[A](request)
    requestBody.fold(Future.successful(Results.BadRequest("Could not parse body"))) { body =>
      for {
        promise <- action(body)
        responseBytes = Pickle.intoBytes(promise)
      } yield Results.Ok(responseBytes.array())
    }
  }

  def getAction[A](action: (() => Future[A]))(implicit a: Pickler[A]) = Action.async { request =>
    for {
      promise <- action()
      responseBytes = Pickle.intoBytes(promise)
    } yield Results.Ok(responseBytes.array())
  }

  private def extractBody[A](request: Request[AnyContent])(implicit u: Pickler[A]): Option[A] = {
    for {
      body  <- request.body.asRaw
      bytes <- body.asBytes()
      dataTry = Try {
        Unpickle[A].fromBytes(bytes.asByteBuffer)
      }
      data <- dataTry match {
        case Success(v) => Some(v)
        case Failure(t) =>
          logger.error("Failed to unpickle request", t)
          None
      }
    } yield data
  }
}
