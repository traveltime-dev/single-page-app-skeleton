package backend.rest.mappers.todto

import shared.dto

trait StringImplicits {
  implicit class StringOps(val self: String) {
    def toResponse: dto.responses.DateResponse = DateMapper.toResponse(self)
  }
}

trait Implicits extends StringImplicits
