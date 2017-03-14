package backend.rest.controllers

import com.google.inject.Singleton
import shared.BuildInfo
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

@Singleton
class VersionController extends Controller {
  val version = Action { _ =>
    val buildInfo = BuildInfo.toMap.mapValues(_.toString)
    Ok(Json.toJson(buildInfo))
  }
}