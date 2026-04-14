package errors

import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.api.http.{DefaultHttpErrorHandler, HttpErrorHandler}
import play.api.libs.json.Json
import play.api.mvc.Results.{BadRequest, InternalServerError, NotFound, Status}
import play.api.mvc.{RequestHeader, Result}
import play.api.routing.Router
import play.api.{Configuration, Environment, OptionalSourceMapper}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ApiErrorHandler @Inject() (
    env: Environment,
    config: Configuration,
    sourceMapper: OptionalSourceMapper,
    router: javax.inject.Provider[Router],
    implicit val ec: ExecutionContext
) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) with HttpErrorHandler with Logging {

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    val payload = ApiError(code = "CLIENT_ERROR", message = message)
    val result = statusCode match {
      case 400 => BadRequest(Json.toJson(payload))
      case 404 => NotFound(Json.toJson(ApiError(code = "NOT_FOUND", message = "Route not found")))
      case other => Status(other)(Json.toJson(payload))
    }
    Future.successful(result)
  }

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    exception match {
      case appEx: AppException =>
        Future.successful(
          Status(appEx.statusCode)(
            Json.toJson(ApiError(code = appEx.code, message = appEx.getMessage, details = appEx.details))
          )
        )
      case other =>
        logger.error("Unhandled server error", other)
        Future.successful(
          InternalServerError(
            Json.toJson(ApiError(code = "INTERNAL_SERVER_ERROR", message = "Unexpected server error"))
          )
        )
    }
  }
}
