package backend.rest.mappers.todto

import shared.dto

object DateMapper {
  def toResponse(response: String): dto.responses.DateResponse = dto.responses.DateResponse(response)
}
