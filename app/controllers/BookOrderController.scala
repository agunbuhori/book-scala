package controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}
import resources.{ApiResponse, BookOrderResource, PaginationMeta}
import services.BookOrderService
import validation.BookOrderRequestValidator

@Singleton
class BookOrderController @Inject() (
    cc: ControllerComponents,
    service: BookOrderService
) extends AbstractController(cc) {

  def create = Action(parse.json) { request =>
    val payload = BookOrderRequestValidator.validateCreate(request.body)
    val created = service.create(payload)
    val resource = BookOrderResource.from(created)

    Created(ApiResponse.created("Order created successfully", Json.toJson(resource)))
      .withHeaders(LOCATION -> s"/api/v1/orders/${created.id}")
  }

  def getById(id: Long) = Action {
    val order = service.getById(id)
    Ok(ApiResponse.ok("Order retrieved successfully", Json.toJson(BookOrderResource.from(order))))
  }

  def list(page: Int, size: Int) = Action {
    val result = service.list(page, size)
    val resources = result.data.map(BookOrderResource.from)
    val meta = PaginationMeta(result.page, result.size, result.totalItems, result.totalPages)

    Ok(ApiResponse.paginated("Orders retrieved successfully", Json.toJson(resources), meta))
  }

  def update(id: Long) = Action(parse.json) { request =>
    val payload = BookOrderRequestValidator.validateUpdate(request.body)
    val updated = service.update(id, payload)

    Ok(ApiResponse.ok("Order updated successfully", Json.toJson(BookOrderResource.from(updated))))
  }

  def delete(id: Long) = Action {
    service.delete(id)
    Ok(ApiResponse.deleted("Order deleted successfully"))
  }
}
