package controllers

import org.scalatestplus.play.PlaySpec
import play.api.mvc.{AnyContent, MessagesControllerComponents}
import play.api.test.FakeRequest
import play.api.test.Helpers.stubMessagesControllerComponents

trait BaseControllerSpec extends PlaySpec {

  implicit val stubMCC: MessagesControllerComponents = stubMessagesControllerComponents()
  implicit val request: FakeRequest[AnyContent] = FakeRequest()

}
