package errors

import play.api.libs.json.{Json, OWrites}

final case class ApiError(
    success: Boolean = false,
    code: String,
    message: String,
    details: Option[String] = None
)

object ApiError {
  implicit val writes: OWrites[ApiError] = Json.writes[ApiError]
}

sealed abstract class AppException(
    val code: String,
    override val getMessage: String,
    val statusCode: Int,
    val details: Option[String] = None
) extends RuntimeException(getMessage)

final case class ValidationException(
    message: String,
    override val details: Option[String] = None
) extends AppException("VALIDATION_ERROR", message, 400, details)

final case class NotFoundException(
    message: String
) extends AppException("NOT_FOUND", message, 404)
