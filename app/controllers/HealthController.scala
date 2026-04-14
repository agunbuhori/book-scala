package controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class HealthController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  def health = Action {
    Ok(Json.obj("status" -> "ok", "service" -> "book-order-api"))
  }
}
