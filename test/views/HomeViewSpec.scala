package views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class HomeViewSpec extends BaseViewSpec {

  val home: Home = inject[Home]
  val document: Document = Jsoup.parse(home().render)

  "Home" must {
    "have a title" in {
      document.title mustBe HomeMessages.title
    }

    "have a h1" in {
      document.selectHead("h1").text mustBe HomeMessages.heading
    }

    "have a first paragraph" in {
      document.selectNth("p", 1).text mustBe HomeMessages.paraOne
    }

    "have a second paragraph" in {
      document.selectNth("p", 2).text mustBe HomeMessages.paraTwo
    }
  }

  object HomeMessages {
    val title: String = "Personal Hub"
    val heading: String = "Personal Hub"
    val paraOne: String = "In this personal hub, I will be playing around with Play!"
    val paraTwo: String = "I intend to develop this in small steps."
  }

}
