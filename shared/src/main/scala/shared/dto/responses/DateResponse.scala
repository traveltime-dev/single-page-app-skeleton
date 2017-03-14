package shared.dto.responses

import shared.dto.pickler.PicklerState._

case class DateResponse(isoDate: String)

object DateResponse {
  implicit val pickler = PicklerGenerator.generatePickler[DateResponse]
}
