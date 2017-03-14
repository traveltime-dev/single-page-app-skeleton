package shared.dto.pickler

import boopickle.{PickleState, PicklerHelper, UnpickleState}

import scalaz.{-\/, \/, \/-}
import scalaz.syntax.either._

trait ScalazPicklers extends PicklerHelper {
  val nullByte: Byte        = 0
  val eitherLeftByte: Byte  = 1
  val eitherRightByte: Byte = 2

  implicit def scalazEitherPickler[T: P, S: P]: P[T \/ S] = new P[T \/ S] {
    override def pickle(obj: T \/ S)(implicit state: PickleState): Unit = {
      obj match {
        case null =>
          state.enc.writeByte(nullByte)
          ()
        case -\/(l) =>
          state.enc.writeByte(eitherLeftByte)
          write[T](l)
        case \/-(r) =>
          state.enc.writeByte(eitherRightByte)
          write[S](r)
      }
    }

    override def unpickle(implicit state: UnpickleState): T \/ S = {
      state.dec.readByte match {
        case `nullByte` =>
          null
        case `eitherLeftByte` =>
          read[T].left
        case `eitherRightByte` =>
          read[S].right
        case _ =>
          throw new IllegalArgumentException("Invalid coding for Scalaz \\/ type")
      }
    }
  }
}
