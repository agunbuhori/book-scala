package validation

import errors.ValidationException
import models.{CreateBookOrderRequest, UpdateBookOrderRequest}
import play.api.libs.json.{JsError, JsValue}

object BookOrderRequestValidator {
  def validateCreate(body: JsValue): CreateBookOrderRequest =
    body.validate[CreateBookOrderRequest].fold(
      errors => throw ValidationException("Invalid request body", Some(JsError.toJson(errors).toString())),
      identity
    )

  def validateUpdate(body: JsValue): UpdateBookOrderRequest =
    body.validate[UpdateBookOrderRequest].fold(
      errors => throw ValidationException("Invalid request body", Some(JsError.toJson(errors).toString())),
      identity
    )
}
