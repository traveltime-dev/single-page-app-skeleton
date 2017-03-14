package shared.dto.pickler

import java.nio.ByteBuffer

import boopickle._

object PicklerState
    extends Base
    with BasicImplicitPicklers
    with TransformPicklers
    with TuplePicklers
    with ScalazPicklers {

  implicit def pickleState   = new PickleState(new EncoderSize, false, false)
  implicit val unpickleState = (bb: ByteBuffer) => new UnpickleState(new DecoderSize(bb), false, false)

  val PicklerGenerator = DefaultBasic.PicklerGenerator
  val CompositePickler = boopickle.CompositePickler
}
