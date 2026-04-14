package resources

import models.BookOrder
import play.api.libs.json.{Json, Writes}

final case class BookOrderResource(
    id: Long,
    customerName: String,
    bookTitle: String,
    quantity: Int,
    orderedAt: String,
    createdAt: String,
    updatedAt: String
)

object BookOrderResource {
  implicit val writes: Writes[BookOrderResource] = Json.writes[BookOrderResource]

  def from(order: BookOrder): BookOrderResource =
    BookOrderResource(
      id = order.id,
      customerName = order.customerName,
      bookTitle = order.bookTitle,
      quantity = order.quantity,
      orderedAt = order.orderDate.toString,
      createdAt = order.createdAt.toString,
      updatedAt = order.updatedAt.toString
    )
}
