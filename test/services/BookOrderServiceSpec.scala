package services

import errors.{NotFoundException, ValidationException}
import models.{BookOrder, CreateBookOrderRequest, PagedResult, UpdateBookOrderRequest}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import repositories.BookOrderRepository

import java.time.LocalDateTime

class BookOrderServiceSpec extends AnyWordSpec with Matchers {

  "BookOrderService" should {
    "create order successfully" in {
      val repo = new InMemoryBookOrderRepository
      val service = new BookOrderService(repo)

      val created = service.create(CreateBookOrderRequest("Andi", "Clean Code", 2))

      created.id shouldBe 1L
      created.customerName shouldBe "Andi"
      created.bookTitle shouldBe "Clean Code"
      created.quantity shouldBe 2
    }

    "throw ValidationException when create payload invalid" in {
      val repo = new InMemoryBookOrderRepository
      val service = new BookOrderService(repo)

      assertThrows[ValidationException] {
        service.create(CreateBookOrderRequest("A", "B", 0))
      }
    }

    "return paged list" in {
      val repo = new InMemoryBookOrderRepository
      val service = new BookOrderService(repo)

      service.create(CreateBookOrderRequest("Andi", "Book A", 1))
      service.create(CreateBookOrderRequest("Budi", "Book B", 2))

      val page = service.list(page = 1, size = 1)

      page.data.size shouldBe 1
      page.totalItems shouldBe 2
      page.totalPages shouldBe 2
      page.page shouldBe 1
      page.size shouldBe 1
    }

    "throw ValidationException when pagination invalid" in {
      val repo = new InMemoryBookOrderRepository
      val service = new BookOrderService(repo)

      assertThrows[ValidationException] {
        service.list(page = 0, size = 10)
      }

      assertThrows[ValidationException] {
        service.list(page = 1, size = 101)
      }
    }

    "update existing order" in {
      val repo = new InMemoryBookOrderRepository
      val service = new BookOrderService(repo)

      val created = service.create(CreateBookOrderRequest("Andi", "Book A", 1))

      val updated = service.update(created.id, UpdateBookOrderRequest("Andi Update", "Book B", 3))

      updated.id shouldBe created.id
      updated.customerName shouldBe "Andi Update"
      updated.bookTitle shouldBe "Book B"
      updated.quantity shouldBe 3
    }

    "throw NotFoundException when updating non-existing order" in {
      val repo = new InMemoryBookOrderRepository
      val service = new BookOrderService(repo)

      assertThrows[NotFoundException] {
        service.update(999L, UpdateBookOrderRequest("Andi", "Book", 1))
      }
    }

    "delete existing order" in {
      val repo = new InMemoryBookOrderRepository
      val service = new BookOrderService(repo)

      val created = service.create(CreateBookOrderRequest("Andi", "Book A", 1))
      service.delete(created.id)

      assertThrows[NotFoundException] {
        service.getById(created.id)
      }
    }

    "throw NotFoundException when deleting non-existing order" in {
      val repo = new InMemoryBookOrderRepository
      val service = new BookOrderService(repo)

      assertThrows[NotFoundException] {
        service.delete(999L)
      }
    }
  }

  private class InMemoryBookOrderRepository extends BookOrderRepository {
    private var sequence: Long = 0L
    private var rows: Vector[BookOrder] = Vector.empty

    override def create(customerName: String, bookTitle: String, quantity: Int): BookOrder = {
      sequence += 1
      val now = LocalDateTime.now()
      val order = BookOrder(
        id = sequence,
        customerName = customerName,
        bookTitle = bookTitle,
        quantity = quantity,
        orderDate = now,
        createdAt = now,
        updatedAt = now
      )
      rows = rows :+ order
      order
    }

    override def findById(id: Long): Option[BookOrder] = rows.find(_.id == id)

    override def findAll(page: Int, size: Int): Seq[BookOrder] = {
      val offset = (page - 1) * size
      rows.sortBy(-_.id).slice(offset, offset + size)
    }

    override def countAll(): Long = rows.size.toLong

    override def update(id: Long, customerName: String, bookTitle: String, quantity: Int): Option[BookOrder] = {
      rows.indexWhere(_.id == id) match {
        case -1 => None
        case index =>
          val now = LocalDateTime.now()
          val current = rows(index)
          val updated = current.copy(
            customerName = customerName,
            bookTitle = bookTitle,
            quantity = quantity,
            orderDate = now,
            updatedAt = now
          )
          rows = rows.updated(index, updated)
          Some(updated)
      }
    }

    override def delete(id: Long): Boolean = {
      val before = rows.size
      rows = rows.filterNot(_.id == id)
      rows.size != before
    }
  }
}
