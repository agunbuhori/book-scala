package services

import javax.inject.{Inject, Singleton}
import errors.{NotFoundException, ValidationException}
import models.{BookOrder, CreateBookOrderRequest, PagedResult, UpdateBookOrderRequest}
import repositories.BookOrderRepository

@Singleton
class BookOrderService @Inject() (repo: BookOrderRepository) {

  def create(request: CreateBookOrderRequest): BookOrder = {
    validateInput(request.customerName, request.bookTitle, request.quantity)
    repo.create(request.customerName.trim, request.bookTitle.trim, request.quantity)
  }

  def getById(id: Long): BookOrder =
    repo.findById(id).getOrElse(throw NotFoundException(s"Book order with id $id not found"))

  def list(page: Int, size: Int): PagedResult[BookOrder] = {
    if (page < 1) throw ValidationException("page must be >= 1")
    if (size < 1 || size > 100) throw ValidationException("size must be in range 1..100")

    val data = repo.findAll(page, size)
    val totalItems = repo.countAll()
    val totalPages = if (totalItems == 0) 0 else math.ceil(totalItems.toDouble / size.toDouble).toInt

    PagedResult(data, page, size, totalItems, totalPages)
  }

  def update(id: Long, request: UpdateBookOrderRequest): BookOrder = {
    validateInput(request.customerName, request.bookTitle, request.quantity)
    repo
      .update(id, request.customerName.trim, request.bookTitle.trim, request.quantity)
      .getOrElse(throw NotFoundException(s"Book order with id $id not found"))
  }

  def delete(id: Long): Unit = {
    val deleted = repo.delete(id)
    if (!deleted) throw NotFoundException(s"Book order with id $id not found")
  }

  private def validateInput(customerName: String, bookTitle: String, quantity: Int): Unit = {
    if (customerName.trim.length < 2) {
      throw ValidationException("customerName must be at least 2 characters")
    }
    if (bookTitle.trim.length < 2) {
      throw ValidationException("bookTitle must be at least 2 characters")
    }
    if (quantity < 1) {
      throw ValidationException("quantity must be >= 1")
    }
  }
}
