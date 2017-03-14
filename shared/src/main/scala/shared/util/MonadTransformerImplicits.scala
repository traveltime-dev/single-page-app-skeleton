package shared.util

import scala.concurrent.Future
import scalaz.{Applicative, EitherT, OptionT, \/}
import scalaz.syntax.std.option._
import scalaz.syntax.either._
import scala.concurrent.ExecutionContext.Implicits.global

object MonadTransformerImplicits {
  implicit def runEitherT[A[_], B, C](self: EitherT[A, B, C]): A[B \/ C] = self.run
  implicit def runOptionT[A[_], B](self: OptionT[A, B]): A[Option[B]]    = self.run

  implicit class IdentityOps[A](val self: A) extends AnyVal {
    def liftOptionT[M[_]](implicit M: Applicative[M])       = OptionT(M.point(self.some))
    def liftEitherT[M[_], LEFT](implicit M: Applicative[M]) = EitherT(M.point(self.right[LEFT]))
  }

  implicit class OptionOps[A](val self: Option[A]) extends AnyVal {
    def liftOptionT[M[_]](implicit M: Applicative[M]) = OptionT(M.point(self))
  }

  implicit class EitherOps[A, B](val self: A \/ B) extends AnyVal {
    def liftEitherT[M[_]](implicit M: Applicative[M]) = EitherT(M.point(self))
  }

  implicit class FutureOps[A](val self: Future[A]) extends AnyVal {
    def liftOptionT       = OptionT(self.map(_.some))
    def liftEitherT[LEFT] = EitherT(self.map(_.right[LEFT]))
  }

  implicit class FutureOptionOps[A](val self: Future[Option[A]]) extends AnyVal {
    def liftOptionT = OptionT(self)
  }

  implicit class FutureEitherOps[A, B](val self: Future[A \/ B]) extends AnyVal {
    def liftEitherT = EitherT(self)
  }
}
