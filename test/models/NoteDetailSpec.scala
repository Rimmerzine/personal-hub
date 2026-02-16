package models

import org.scalatestplus.play.PlaySpec

class NoteDetailSpec extends PlaySpec {

  "bodySummary" should {
    "return the full body" when {
      "it is less than 150 characters" in {
        NoteDetail("", "a" * 149).bodySummary mustBe "a" * 149
      }
      "it is exactly 150 characters" in {
        NoteDetail("", "a" * 150).bodySummary mustBe "a" * 150
      }
    }
    "return a body summary" when {
      "it is more than 150 characters" in {
        NoteDetail("", "a" * 151).bodySummary mustBe "a" * 147 + "..."
      }
    }
  }

}
