package resources

import play.api.libs.json.{JsValue, Json, Writes}

final case class PaginationMeta(
    page: Int,
    size: Int,
    totalItems: Long,
    totalPages: Int
)

object PaginationMeta {
  implicit val writes: Writes[PaginationMeta] = Json.writes[PaginationMeta]
}

object ApiResponse {
  def ok(message: String, data: JsValue): JsValue =
    Json.obj(
      "success" -> true,
      "message" -> message,
      "data" -> data
    )

  def created(message: String, data: JsValue): JsValue =
    Json.obj(
      "success" -> true,
      "message" -> message,
      "data" -> data
    )

  def deleted(message: String): JsValue =
    Json.obj(
      "success" -> true,
      "message" -> message
    )

  def paginated(message: String, data: JsValue, meta: PaginationMeta): JsValue =
    Json.obj(
      "success" -> true,
      "message" -> message,
      "data" -> data,
      "meta" -> Json.toJson(meta)
    )
}
