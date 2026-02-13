package repositories.utils

import scala.concurrent.ExecutionContext.Implicits.global

class MongoComponentSpec extends RepositorySpec {

  val mongoComponent: MongoComponent = application.injector.instanceOf[MongoComponent]

  "MongoComponent" should {

    "provide a valid MongoDatabase" in {
      mongoComponent.database.name mustBe "test-base-repo"
      mongoComponent.database.listCollectionNames().toFuture().map(_ => succeed)
    }

  }

}
