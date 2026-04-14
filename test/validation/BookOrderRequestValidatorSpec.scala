package validation

import errors.ValidationException
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.Json

class BookOrderRequestValidatorSpec extends AnyWordSpec with Matchers {

  "BookOrderRequestValidator" should {
    "validate create payload successfully" in {
      val body = Json.obj(
        "customerName" -> "Andi",
        "bookTitle" -> "Domain-Driven Design",
        "quantity" -> 2
      )

      val result = BookOrderRequestValidator.validateCreate(body)

      result.customerName shouldBe "Andi"
      result.bookTitle shouldBe "Domain-Driven Design"
      result.quantity shouldBe 2
    }

    "throw ValidationException for invalid create payload" in {
      val body = Json.obj(
        "customerName" -> "A",
        "bookTitle" -> "",
        "quantity" -> 0
      )

      assertThrows[ValidationException] {
        BookOrderRequestValidator.validateCreate(body)
      }
    }

    "validate update payload successfully" in {
      val body = Json.obj(
        "customerName" -> "Budi",
        "bookTitle" -> "Scala in Action",
        "quantity" -> 3
      )

      val result = BookOrderRequestValidator.validateUpdate(body)

      result.customerName shouldBe "Budi"
      result.bookTitle shouldBe "Scala in Action"
      result.quantity shouldBe 3
    }

    "throw ValidationException for invalid update payload" in {
      val body = Json.obj(
        "customerName" -> "",
        "bookTitle" -> "B",
        "quantity" -> -1
      )

      assertThrows[ValidationException] {
        BookOrderRequestValidator.validateUpdate(body)
      }
    }
  }
}
