// @GENERATOR:play-routes-compiler
// @SOURCE:conf/routes

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:2
  HealthController_0: controllers.HealthController,
  // @LINE:5
  BookOrderController_1: controllers.BookOrderController,
  val prefix: String
) extends GeneratedRouter {

  @javax.inject.Inject()
  def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:2
    HealthController_0: controllers.HealthController,
    // @LINE:5
    BookOrderController_1: controllers.BookOrderController
  ) = this(errorHandler, HealthController_0, BookOrderController_1, "/")

  def withPrefix(addPrefix: String): Routes = {
    val prefix = play.api.routing.Router.concatPrefix(addPrefix, this.prefix)
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, HealthController_0, BookOrderController_1, prefix)
  }

  private val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/v1/health""", """controllers.HealthController.health"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/v1/orders""", """controllers.BookOrderController.create"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/v1/orders""", """controllers.BookOrderController.list(page:Int ?= 1, size:Int ?= 10)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/v1/orders/""" + "$" + """id<[^/]+>""", """controllers.BookOrderController.getById(id:Long)"""),
    ("""PUT""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/v1/orders/""" + "$" + """id<[^/]+>""", """controllers.BookOrderController.update(id:Long)"""),
    ("""DELETE""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """api/v1/orders/""" + "$" + """id<[^/]+>""", """controllers.BookOrderController.delete(id:Long)"""),
    Nil
  ).foldLeft(Seq.empty[(String, String, String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String, String, String)]
    case l => s ++ l.asInstanceOf[List[(String, String, String)]]
  }}


  // @LINE:2
  private lazy val controllers_HealthController_health0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/v1/health")))
  )
  private lazy val controllers_HealthController_health0_invoker = createInvoker(
    HealthController_0.health,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HealthController",
      "health",
      Nil,
      "GET",
      this.prefix + """api/v1/health""",
      """ Health endpoint""",
      Seq()
    )
  )

  // @LINE:5
  private lazy val controllers_BookOrderController_create1_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/v1/orders")))
  )
  private lazy val controllers_BookOrderController_create1_invoker = createInvoker(
    BookOrderController_1.create,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.BookOrderController",
      "create",
      Nil,
      "POST",
      this.prefix + """api/v1/orders""",
      """ Book order REST endpoints""",
      Seq()
    )
  )

  // @LINE:6
  private lazy val controllers_BookOrderController_list2_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/v1/orders")))
  )
  private lazy val controllers_BookOrderController_list2_invoker = createInvoker(
    BookOrderController_1.list(fakeValue[Int], fakeValue[Int]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.BookOrderController",
      "list",
      Seq(classOf[Int], classOf[Int]),
      "GET",
      this.prefix + """api/v1/orders""",
      """""",
      Seq()
    )
  )

  // @LINE:7
  private lazy val controllers_BookOrderController_getById3_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/v1/orders/"), DynamicPart("id", """[^/]+""", encodeable=true)))
  )
  private lazy val controllers_BookOrderController_getById3_invoker = createInvoker(
    BookOrderController_1.getById(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.BookOrderController",
      "getById",
      Seq(classOf[Long]),
      "GET",
      this.prefix + """api/v1/orders/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:8
  private lazy val controllers_BookOrderController_update4_route = Route("PUT",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/v1/orders/"), DynamicPart("id", """[^/]+""", encodeable=true)))
  )
  private lazy val controllers_BookOrderController_update4_invoker = createInvoker(
    BookOrderController_1.update(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.BookOrderController",
      "update",
      Seq(classOf[Long]),
      "PUT",
      this.prefix + """api/v1/orders/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:9
  private lazy val controllers_BookOrderController_delete5_route = Route("DELETE",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("api/v1/orders/"), DynamicPart("id", """[^/]+""", encodeable=true)))
  )
  private lazy val controllers_BookOrderController_delete5_invoker = createInvoker(
    BookOrderController_1.delete(fakeValue[Long]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.BookOrderController",
      "delete",
      Seq(classOf[Long]),
      "DELETE",
      this.prefix + """api/v1/orders/""" + "$" + """id<[^/]+>""",
      """""",
      Seq()
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:2
    case controllers_HealthController_health0_route(params@_) =>
      call { 
        controllers_HealthController_health0_invoker.call(HealthController_0.health)
      }
  
    // @LINE:5
    case controllers_BookOrderController_create1_route(params@_) =>
      call { 
        controllers_BookOrderController_create1_invoker.call(BookOrderController_1.create)
      }
  
    // @LINE:6
    case controllers_BookOrderController_list2_route(params@_) =>
      call(params.fromQuery[Int]("page", Some(1)), params.fromQuery[Int]("size", Some(10))) { (page, size) =>
        controllers_BookOrderController_list2_invoker.call(BookOrderController_1.list(page, size))
      }
  
    // @LINE:7
    case controllers_BookOrderController_getById3_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_BookOrderController_getById3_invoker.call(BookOrderController_1.getById(id))
      }
  
    // @LINE:8
    case controllers_BookOrderController_update4_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_BookOrderController_update4_invoker.call(BookOrderController_1.update(id))
      }
  
    // @LINE:9
    case controllers_BookOrderController_delete5_route(params@_) =>
      call(params.fromPath[Long]("id", None)) { (id) =>
        controllers_BookOrderController_delete5_invoker.call(BookOrderController_1.delete(id))
      }
  }
}
