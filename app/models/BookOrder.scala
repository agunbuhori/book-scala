package models

import java.time.LocalDateTime
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.{Format, JsPath, Json, Reads}
import play.api.libs.json.Reads.min

final case class BookOrder(
    id: Long,
    customerName: String,
    bookTitle: String,
    quantity: Int,
    orderDate: LocalDateTime,
    createdAt: LocalDateTime,
    updatedAt: LocalDateTime
)

object BookOrder {
  implicit val format: Format[BookOrder] = Json.format[BookOrder]
}

final case class CreateBookOrderRequest(
    customerName: String,
    bookTitle: String,
    quantity: Int
)

object CreateBookOrderRequest {
  implicit val reads: Reads[CreateBookOrderRequest] = (
    (JsPath \ "customerName").read[String](Reads.minLength[String](2)) and
      (JsPath \ "bookTitle").read[String](Reads.minLength[String](2)) and
      (JsPath \ "quantity").read[Int](min(1))
  )(CreateBookOrderRequest.apply _)
}

final case class UpdateBookOrderRequest(
    customerName: String,
    bookTitle: String,
    quantity: Int
)

object UpdateBookOrderRequest {
  implicit val reads: Reads[UpdateBookOrderRequest] = (
    (JsPath \ "customerName").read[String](Reads.minLength[String](2)) and
      (JsPath \ "bookTitle").read[String](Reads.minLength[String](2)) and
      (JsPath \ "quantity").read[Int](min(1))
  )(UpdateBookOrderRequest.apply _)
}

final case class PagedResult[A](
    data: Seq[A],
    page: Int,
    size: Int,
    totalItems: Long,
    totalPages: Int
)

object PagedResult {
  implicit def writes[A: play.api.libs.json.Writes]: play.api.libs.json.OWrites[PagedResult[A]] =
    Json.writes[PagedResult[A]]
}
