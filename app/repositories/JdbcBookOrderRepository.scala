package repositories

import java.sql.{Statement, Timestamp}
import java.time.LocalDateTime
import javax.inject.{Inject, Singleton}
import play.api.db.Database
import models.BookOrder

@Singleton
class JdbcBookOrderRepository @Inject() (db: Database) extends BookOrderRepository {

  private def toOrder(rs: java.sql.ResultSet): BookOrder =
    BookOrder(
      id = rs.getLong("id"),
      customerName = rs.getString("customer_name"),
      bookTitle = rs.getString("book_title"),
      quantity = rs.getInt("quantity"),
      orderDate = rs.getTimestamp("order_date").toLocalDateTime,
      createdAt = rs.getTimestamp("created_at").toLocalDateTime,
      updatedAt = rs.getTimestamp("updated_at").toLocalDateTime
    )

  override def create(customerName: String, bookTitle: String, quantity: Int): BookOrder = {
    db.withConnection { conn =>
      val now = LocalDateTime.now()
      val sql =
        """
          |INSERT INTO book_orders (customer_name, book_title, quantity, order_date, created_at, updated_at)
          |VALUES (?, ?, ?, ?, ?, ?)
          |""".stripMargin

      val stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
      stmt.setString(1, customerName)
      stmt.setString(2, bookTitle)
      stmt.setInt(3, quantity)
      stmt.setTimestamp(4, Timestamp.valueOf(now))
      stmt.setTimestamp(5, Timestamp.valueOf(now))
      stmt.setTimestamp(6, Timestamp.valueOf(now))
      stmt.executeUpdate()

      val keys = stmt.getGeneratedKeys
      if (keys.next()) {
        val id = keys.getLong(1)
        findById(id).get
      } else {
        throw new RuntimeException("Failed to create book order")
      }
    }
  }

  override def findById(id: Long): Option[BookOrder] = {
    db.withConnection { conn =>
      val stmt = conn.prepareStatement(
        "SELECT id, customer_name, book_title, quantity, order_date, created_at, updated_at FROM book_orders WHERE id = ?"
      )
      stmt.setLong(1, id)
      val rs = stmt.executeQuery()
      if (rs.next()) Some(toOrder(rs)) else None
    }
  }

  override def findAll(page: Int, size: Int): Seq[BookOrder] = {
    db.withConnection { conn =>
      val offset = (page - 1) * size
      val stmt = conn.prepareStatement(
        "SELECT id, customer_name, book_title, quantity, order_date, created_at, updated_at FROM book_orders ORDER BY id DESC LIMIT ? OFFSET ?"
      )
      stmt.setInt(1, size)
      stmt.setInt(2, offset)
      val rs = stmt.executeQuery()
      val orders = scala.collection.mutable.ArrayBuffer.empty[BookOrder]
      while (rs.next()) {
        orders += toOrder(rs)
      }
      orders.toSeq
    }
  }

  override def countAll(): Long = {
    db.withConnection { conn =>
      val stmt = conn.prepareStatement("SELECT COUNT(1) AS total FROM book_orders")
      val rs = stmt.executeQuery()
      if (rs.next()) rs.getLong("total") else 0L
    }
  }

  override def update(id: Long, customerName: String, bookTitle: String, quantity: Int): Option[BookOrder] = {
    db.withConnection { conn =>
      val now = LocalDateTime.now()
      val stmt = conn.prepareStatement(
        "UPDATE book_orders SET customer_name = ?, book_title = ?, quantity = ?, order_date = ?, updated_at = ? WHERE id = ?"
      )
      stmt.setString(1, customerName)
      stmt.setString(2, bookTitle)
      stmt.setInt(3, quantity)
      stmt.setTimestamp(4, Timestamp.valueOf(now))
      stmt.setTimestamp(5, Timestamp.valueOf(now))
      stmt.setLong(6, id)
      val rows = stmt.executeUpdate()
      if (rows > 0) findById(id) else None
    }
  }

  override def delete(id: Long): Boolean = {
    db.withConnection { conn =>
      val stmt = conn.prepareStatement("DELETE FROM book_orders WHERE id = ?")
      stmt.setLong(1, id)
      stmt.executeUpdate() > 0
    }
  }
}
