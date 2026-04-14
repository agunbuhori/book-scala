package repositories

import models.BookOrder

trait BookOrderRepository {
  def create(customerName: String, bookTitle: String, quantity: Int): BookOrder
  def findById(id: Long): Option[BookOrder]
  def findAll(page: Int, size: Int): Seq[BookOrder]
  def countAll(): Long
  def update(id: Long, customerName: String, bookTitle: String, quantity: Int): Option[BookOrder]
  def delete(id: Long): Boolean
}
