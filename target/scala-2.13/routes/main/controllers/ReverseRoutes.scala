// @GENERATOR:play-routes-compiler
// @SOURCE:conf/routes

import play.api.mvc.Call


import _root_.controllers.Assets.Asset

// @LINE:2
package controllers {

  // @LINE:2
  class ReverseHealthController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:2
    def health: Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/v1/health")
    }
  
  }

  // @LINE:5
  class ReverseBookOrderController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:9
    def delete(id:Long): Call = {
      
      Call("DELETE", _prefix + { _defaultPrefix } + "api/v1/orders/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:5
    def create: Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "api/v1/orders")
    }
  
    // @LINE:6
    def list(page:Int = 1, size:Int = 10): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/v1/orders" + play.core.routing.queryString(List(if(page == 1) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("page", page)), if(size == 10) None else Some(implicitly[play.api.mvc.QueryStringBindable[Int]].unbind("size", size)))))
    }
  
    // @LINE:8
    def update(id:Long): Call = {
      
      Call("PUT", _prefix + { _defaultPrefix } + "api/v1/orders/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
    // @LINE:7
    def getById(id:Long): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "api/v1/orders/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[Long]].unbind("id", id)))
    }
  
  }


}
