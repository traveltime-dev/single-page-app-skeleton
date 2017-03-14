package backend.rest.controllers

import com.google.inject.{Inject, Singleton}
import backend.core.service.DateService
import play.api.mvc.Controller
import shared.dto

import scala.concurrent.Future

@Singleton
class DateController @Inject()(dateService: DateService) extends Controller {
  val date = getAction[dto.responses.DateResponse] { () =>
    Future.successful(dateService.date().toResponse)
  }
}
