package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import views.html.Home

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "HomeController GET" should {

    "render the Index page from a new instance of controller" in {
      val homeView: Home = inject[Home]
      val controller = new HomeController(homeView, stubMessagesControllerComponents())
      val home = controller.show().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("home.heading") // TODO: not great, stubMessagesControllerComponents provides fake messages
    }

    "render the Index page from the application" in {
      val controller = inject[HomeController]
      val home = controller.show().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Personal Hub")
      contentAsString(home) must include ("In this personal hub, I will be playing around with Play!")
      contentAsString(home) must include ("I intend to develop this in small steps.")
    }

    "render the Index page from the router" in {
      val request = FakeRequest(GET, "/")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Personal Hub")
      contentAsString(home) must include ("In this personal hub, I will be playing around with Play!")
      contentAsString(home) must include ("I intend to develop this in small steps.")
    }
  }
}
