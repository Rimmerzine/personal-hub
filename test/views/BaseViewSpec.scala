package views

import org.jsoup.nodes.Element
import org.scalatest.Checkpoints.Checkpoint
import org.scalatest.{Assertion, Succeeded}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.{Messages, MessagesApi}
import play.api.mvc.{AnyContent, Call}
import play.api.test.{FakeRequest, Injecting}

import scala.jdk.CollectionConverters.CollectionHasAsScala

trait BaseViewSpec extends PlaySpec with GuiceOneAppPerSuite with Injecting {

  implicit val request: FakeRequest[AnyContent] = FakeRequest()
  implicit val messages: Messages = inject[MessagesApi].preferred(request)

  val testCall = Call("POST", "/")

  implicit class ElementSelectors(element: Element) {

    def selectSeq(selector: String): Seq[Element] = {
      element.select(selector).asScala.toSeq
    }

    def selectOptionally(selector: String): Option[Element] = {
      selectSeq(selector).headOption
    }

    def selectHead(selector: String): Element = {
      selectOptionally(selector) match {
        case Some(value) => value
        case None => fail(s"No elements returned for selector $selector")
      }
    }

    def selectNth(selector: String, nth: Int): Element = {
      selectSeq(selector).lift(nth - 1) match {
        case Some(e) => e
        case None => fail(s"Could not retrieve $selector number $nth")
      }
    }


  }

  implicit class ElementTests(element: Element) {

    def mustHaveTextInput(selector: String)
                         (name: String,
                          fieldLabel: String,
                          maybeValue: Option[String] = None,
                          maybePlaceholderText: Option[String] = None,
                          maybeErrorText: Option[String] = None): Assertion = {

      val textField: Element = element.selectHead(selector)
      val checkpoint: Checkpoint = new Checkpoint()

      checkpoint {
        val label: Element = textField.selectHead("label")

        label.text mustBe fieldLabel
        label.attr("for") mustBe name
      }

      checkpoint {
        val input: Element = textField.selectHead("input")

        input.id mustBe name
        input.attr("name") mustBe name
        input.attr("type") mustBe "text"

        maybeValue match {
          case Some(value) => input.attr("value") mustBe value
          case None => input.hasAttr("value") mustBe false
        }

        maybePlaceholderText match {
          case Some(placeholderText) => input.attr("placeholder") mustBe placeholderText
          case None => input.hasAttr("placeholder") mustBe false
        }

        maybeErrorText match {
          case Some(_) => input.attr("aria-describedby") mustBe s"$name-error"
          case None => input.hasAttr("aria-describedby") mustBe false
        }
      }

      checkpoint {
        maybeErrorText match {
          case Some(errorText) =>
            val visualError = textField.selectHead("div.invalid-feedback")

            visualError.id mustBe s"$name-error"
            visualError.text mustBe errorText
          case None =>
            textField.selectOptionally("div.invalid-feedback") mustBe None
        }
        maybeErrorText foreach { error =>
          val visualError = textField.selectHead("div.invalid-feedback")

          visualError.id mustBe s"$name-error"
          visualError.text mustBe error
        }
      }

      checkpoint.reportAll()

      Succeeded

    }

    def mustHaveTextArea(selector: String)
                        (name: String,
                         fieldLabel: String,
                         maybeValue: Option[String] = None,
                         maybeErrorText: Option[String] = None): Assertion = {

      val textField: Element = element.selectHead(selector)
      val checkpoint: Checkpoint = new Checkpoint()

      checkpoint {
        val label: Element = textField.selectHead("label")

        label.text mustBe fieldLabel
        label.attr("for") mustBe name
      }

      checkpoint {
        val input: Element = textField.selectHead("textarea")

        input.id mustBe name
        input.attr("name") mustBe name
        input.attr("type") mustBe "text"

        maybeValue match {
          case Some(value) => input.text mustBe value
          case None => input.text mustBe ""
        }

        maybeErrorText match {
          case Some(_) => input.attr("aria-describedby") mustBe s"$name-error"
          case None => input.hasAttr("aria-describedby") mustBe false
        }
      }

      checkpoint {
        maybeErrorText match {
          case Some(errorText) =>
            val visualError = textField.selectHead("div.invalid-feedback")

            visualError.id mustBe s"$name-error"
            visualError.text mustBe errorText
          case None =>
            textField.selectOptionally("div.invalid-feedback") mustBe None
        }
        maybeErrorText foreach { error =>
          val visualError = textField.selectHead("div.invalid-feedback")

          visualError.id mustBe s"$name-error"
          visualError.text mustBe error
        }
      }

      checkpoint.reportAll()

      Succeeded

    }

  }


}
