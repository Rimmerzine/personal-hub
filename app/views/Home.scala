package views

import play.api.i18n.Messages
import scalatags.Text.TypedTag
import scalatags.Text.all._

import javax.inject.{Inject, Singleton}

@Singleton
class Home @Inject()(main: Main) {

  def apply()(implicit messages: Messages): TypedTag[String] = {
    main(pageTitle = messages("home.heading"))(
      h1(messages("home.heading")),
      p(messages("home.para-one")),
      p(messages("home.para-two"))
    )
  }

}
