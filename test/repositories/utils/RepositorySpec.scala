package repositories.utils

import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceFakeApplicationFactory
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.{Application, Configuration}

trait RepositorySpec extends PlaySpec
  with GuiceFakeApplicationFactory
  with ScalaFutures
  with BeforeAndAfterEach {

  lazy val application: Application = GuiceApplicationBuilder(
    configuration = Configuration.from(
      Map(
        "mongodb.uri" -> "mongodb://localhost:27017",
        "mongodb.name" -> "test-base-repo"
      )
    )
  ).build()

}
