import com.google.inject.AbstractModule
import repositories.{BookOrderRepository, JdbcBookOrderRepository}

class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[BookOrderRepository]).to(classOf[JdbcBookOrderRepository])
  }
}
