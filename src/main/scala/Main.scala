import java.sql.{Connection, Date, DriverManager}
import java.time.LocalDate
import scala.io.StdIn
import scala.util.Try
import scala.util.Using

final case class BookOrder(
        id: Int,
        customerName: String,
        bookTitle: String,
        quantity: Int,
        orderDate: LocalDate
)

object DatabaseConfig {
    val url: String = sys.env.getOrElse(
        "DB_URL",
        "jdbc:mysql://localhost:3306/pemesanan_buku?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
    )
    val user: String = sys.env.getOrElse("DB_USER", "root")
    val password: String = sys.env.getOrElse("DB_PASSWORD", "")
}

object Database {
    Class.forName("com.mysql.cj.jdbc.Driver")

    def getConnection: Connection =
        DriverManager.getConnection(DatabaseConfig.url, DatabaseConfig.user, DatabaseConfig.password)

    def initSchema(): Unit = {
        val createTableSql =
            """
                |CREATE TABLE IF NOT EXISTS book_orders (
                |  id INT PRIMARY KEY AUTO_INCREMENT,
                |  customer_name VARCHAR(100) NOT NULL,
                |  book_title VARCHAR(150) NOT NULL,
                |  quantity INT NOT NULL,
                |  order_date DATE NOT NULL,
                |  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                |)
                |""".stripMargin

        Using.resource(getConnection) { conn =>
            Using.resource(conn.createStatement()) { stmt =>
                stmt.execute(createTableSql)
            }
        }
    }
}

object BookOrderRepository {
    def create(customerName: String, bookTitle: String, quantity: Int, orderDate: LocalDate): Int = {
        val sql =
            "INSERT INTO book_orders (customer_name, book_title, quantity, order_date) VALUES (?, ?, ?, ?)"

        Using.resource(Database.getConnection) { conn =>
            Using.resource(conn.prepareStatement(sql)) { stmt =>
                stmt.setString(1, customerName)
                stmt.setString(2, bookTitle)
                stmt.setInt(3, quantity)
                stmt.setDate(4, Date.valueOf(orderDate))
                stmt.executeUpdate()
            }
        }
    }

    def findAll(): List[BookOrder] = {
        val sql = "SELECT id, customer_name, book_title, quantity, order_date FROM book_orders ORDER BY id"

        Using.resource(Database.getConnection) { conn =>
            Using.resource(conn.prepareStatement(sql)) { stmt =>
                Using.resource(stmt.executeQuery()) { rs =>
                    Iterator
                        .continually(rs)
                        .takeWhile(_.next())
                        .map { row =>
                            BookOrder(
                                id = row.getInt("id"),
                                customerName = row.getString("customer_name"),
                                bookTitle = row.getString("book_title"),
                                quantity = row.getInt("quantity"),
                                orderDate = row.getDate("order_date").toLocalDate
                            )
                        }
                        .toList
                }
            }
        }
    }

    def findById(id: Int): Option[BookOrder] = {
        val sql = "SELECT id, customer_name, book_title, quantity, order_date FROM book_orders WHERE id = ?"

        Using.resource(Database.getConnection) { conn =>
            Using.resource(conn.prepareStatement(sql)) { stmt =>
                stmt.setInt(1, id)
                Using.resource(stmt.executeQuery()) { rs =>
                    if (rs.next()) {
                        Some(
                            BookOrder(
                                id = rs.getInt("id"),
                                customerName = rs.getString("customer_name"),
                                bookTitle = rs.getString("book_title"),
                                quantity = rs.getInt("quantity"),
                                orderDate = rs.getDate("order_date").toLocalDate
                            )
                        )
                    } else {
                        None
                    }
                }
            }
        }
    }

    def update(id: Int, customerName: String, bookTitle: String, quantity: Int, orderDate: LocalDate): Int = {
        val sql =
            "UPDATE book_orders SET customer_name = ?, book_title = ?, quantity = ?, order_date = ? WHERE id = ?"

        Using.resource(Database.getConnection) { conn =>
            Using.resource(conn.prepareStatement(sql)) { stmt =>
                stmt.setString(1, customerName)
                stmt.setString(2, bookTitle)
                stmt.setInt(3, quantity)
                stmt.setDate(4, Date.valueOf(orderDate))
                stmt.setInt(5, id)
                stmt.executeUpdate()
            }
        }
    }

    def delete(id: Int): Int = {
        val sql = "DELETE FROM book_orders WHERE id = ?"

        Using.resource(Database.getConnection) { conn =>
            Using.resource(conn.prepareStatement(sql)) { stmt =>
                stmt.setInt(1, id)
                stmt.executeUpdate()
            }
        }
    }
}

object Main extends App {
    try {
        Database.initSchema()
        println("Terhubung ke MySQL. Tabel book_orders siap.")
        runMenu()
    } catch {
        case e: Exception =>
            println(s"Gagal inisialisasi aplikasi: ${e.getMessage}")
    }

    def runMenu(): Unit = {
        var isRunning = true

        while (isRunning) {
            println("\n=== Aplikasi Pemesanan Buku (CRUD) ===")
            println("1. Tambah pesanan")
            println("2. Lihat semua pesanan")
            println("3. Lihat detail pesanan")
            println("4. Ubah pesanan")
            println("5. Hapus pesanan")
            println("0. Keluar")
            print("Pilih menu: ")

            StdIn.readLine() match {
                case "1" => createOrder()
                case "2" => listOrders()
                case "3" => getOrderById()
                case "4" => updateOrder()
                case "5" => deleteOrder()
                case "0" =>
                    isRunning = false
                    println("Sampai jumpa.")
                case _ => println("Pilihan tidak valid.")
            }
        }
    }

    def createOrder(): Unit = {
        val customerName = readRequired("Nama pelanggan")
        val bookTitle = readRequired("Judul buku")
        val quantity = readPositiveInt("Jumlah")
        val orderDate = readDate("Tanggal pesanan (format YYYY-MM-DD)")

        val affected = BookOrderRepository.create(customerName, bookTitle, quantity, orderDate)
        if (affected > 0) {
            println("Pesanan berhasil ditambahkan.")
        } else {
            println("Pesanan gagal ditambahkan.")
        }
    }

    def listOrders(): Unit = {
        val orders = BookOrderRepository.findAll()
        if (orders.isEmpty) {
            println("Belum ada data pesanan.")
        } else {
            println("\nDaftar pesanan:")
            orders.foreach { order =>
                println(
                    s"ID: ${order.id}, Pelanggan: ${order.customerName}, Buku: ${order.bookTitle}, Jumlah: ${order.quantity}, Tanggal: ${order.orderDate}"
                )
            }
        }
    }

    def getOrderById(): Unit = {
        val id = readPositiveInt("Masukkan ID pesanan")
        BookOrderRepository.findById(id) match {
            case Some(order) =>
                println("\nDetail pesanan:")
                println(s"ID         : ${order.id}")
                println(s"Pelanggan  : ${order.customerName}")
                println(s"Judul Buku : ${order.bookTitle}")
                println(s"Jumlah     : ${order.quantity}")
                println(s"Tanggal    : ${order.orderDate}")
            case None =>
                println("Pesanan tidak ditemukan.")
        }
    }

    def updateOrder(): Unit = {
        val id = readPositiveInt("Masukkan ID pesanan yang ingin diubah")

        BookOrderRepository.findById(id) match {
            case None => println("Pesanan tidak ditemukan.")
            case Some(current) =>
                println("Kosongkan input jika ingin mempertahankan nilai lama.")
                val customerName = readOptional(s"Nama pelanggan [${current.customerName}]").getOrElse(current.customerName)
                val bookTitle = readOptional(s"Judul buku [${current.bookTitle}]").getOrElse(current.bookTitle)
                val quantity = readOptionalPositiveInt(s"Jumlah [${current.quantity}]").getOrElse(current.quantity)
                val orderDate =
                    readOptionalDate(s"Tanggal pesanan (YYYY-MM-DD) [${current.orderDate}]").getOrElse(current.orderDate)

                val affected = BookOrderRepository.update(id, customerName, bookTitle, quantity, orderDate)
                if (affected > 0) {
                    println("Pesanan berhasil diubah.")
                } else {
                    println("Pesanan gagal diubah.")
                }
        }
    }

    def deleteOrder(): Unit = {
        val id = readPositiveInt("Masukkan ID pesanan yang ingin dihapus")
        val affected = BookOrderRepository.delete(id)
        if (affected > 0) {
            println("Pesanan berhasil dihapus.")
        } else {
            println("Pesanan tidak ditemukan atau gagal dihapus.")
        }
    }

    def readRequired(label: String): String = {
        var value = ""
        while (value.trim.isEmpty) {
            print(s"$label: ")
            value = StdIn.readLine()
            if (value == null || value.trim.isEmpty) {
                println(s"$label wajib diisi.")
                value = ""
            }
        }
        value.trim
    }

    def readOptional(label: String): Option[String] = {
        print(s"$label: ")
        Option(StdIn.readLine()).map(_.trim).filter(_.nonEmpty)
    }

    def readPositiveInt(label: String): Int = {
        var result: Option[Int] = None
        while (result.isEmpty) {
            print(s"$label: ")
            val parsed = Option(StdIn.readLine()).flatMap(v => Try(v.trim.toInt).toOption)
            parsed match {
                case Some(value) if value > 0 => result = Some(value)
                case _ => println("Masukkan angka bulat positif.")
            }
        }
        result.get
    }

    def readOptionalPositiveInt(label: String): Option[Int] = {
        var done = false
        var result: Option[Int] = None
        while (!done) {
            print(s"$label: ")
            Option(StdIn.readLine()).map(_.trim) match {
                case Some("") | None =>
                    done = true
                case Some(value) =>
                    Try(value.toInt).toOption match {
                        case Some(v) if v > 0 =>
                            result = Some(v)
                            done = true
                        case _ =>
                            println("Masukkan angka bulat positif atau kosongkan input.")
                    }
            }
        }
        result
    }

    def readDate(label: String): LocalDate = {
        var result: Option[LocalDate] = None
        while (result.isEmpty) {
            print(s"$label: ")
            val parsed = Option(StdIn.readLine()).flatMap(v => Try(LocalDate.parse(v.trim)).toOption)
            parsed match {
                case Some(value) => result = Some(value)
                case None => println("Format tanggal tidak valid. Contoh: 2026-04-14")
            }
        }
        result.get
    }

    def readOptionalDate(label: String): Option[LocalDate] = {
        var done = false
        var result: Option[LocalDate] = None
        while (!done) {
            print(s"$label: ")
            Option(StdIn.readLine()).map(_.trim) match {
                case Some("") | None =>
                    done = true
                case Some(value) =>
                    Try(LocalDate.parse(value)).toOption match {
                        case Some(date) =>
                            result = Some(date)
                            done = true
                        case None =>
                            println("Format tanggal tidak valid. Contoh: 2026-04-14 atau kosongkan input.")
                    }
            }
        }
        result
    }
}