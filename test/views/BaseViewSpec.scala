package views

import org.jsoup.nodes.Element
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.{Messages, MessagesApi}
import play.api.mvc.AnyContent
import play.api.test.{FakeRequest, Injecting}

import scala.jdk.CollectionConverters.CollectionHasAsScala

trait BaseViewSpec extends PlaySpec with GuiceOneAppPerSuite with Injecting {

  implicit val request: FakeRequest[AnyContent] = FakeRequest()
  implicit val messages: Messages = inject[MessagesApi].preferred(request)

  implicit class ElementSelectors(element: Element) {

    def selectSeq(selector: String): Seq[Element] = {
      element.select(selector).asScala.toSeq
    }

    def selectHead(selector: String): Element = {
      selectSeq(selector).headOption match {
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

}
