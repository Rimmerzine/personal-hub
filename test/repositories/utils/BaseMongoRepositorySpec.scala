package repositories.utils

import org.bson.conversions.Bson
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Projections.exclude
import org.scalatest.RecoverMethods.recoverToSucceededIf
import play.api.libs.json.{Json, OFormat}

import scala.concurrent.ExecutionContext.Implicits.global

class BaseMongoRepositorySpec extends RepositorySpec {

  override def beforeEach(): Unit = {
    super.beforeEach()

    TestRepository.mongoCollection.drop().toFuture().futureValue
  }

  "insert" should {

    "return the inserted entity and persist the entity in the collection" in {
      TestRepository.insert(sampleEntity).futureValue mustBe sampleEntity

      TestRepository.findOne(filter).futureValue mustBe Some(sampleEntity)
    }

    "allow inserting multiple entities" in {
      val second = TestEntity("title2", "body2")

      TestRepository.insert(sampleEntity).futureValue
      TestRepository.insert(second).futureValue

      TestRepository.count(Filters.exists("title")).futureValue mustBe 2
    }

  }

  "findOne" should {

    "return Some(entity) when a matching document exists" in {
      TestRepository.insert(sampleEntity).futureValue

      TestRepository.findOne(filter).futureValue mustBe Some(sampleEntity)
    }

    "return None when no document matches" in {
      TestRepository.findOne(filter).futureValue mustBe None
    }

    "fail to return the entity when projection removes required fields" in {
      TestRepository.insert(sampleEntity).futureValue

      recoverToSucceededIf[RuntimeException] {
        TestRepository.findOne(
          filter,
          projection = Some(exclude("body"))
        )
      }
    }

  }

  "find" should {

    "return matching entities" in {
      val second = TestEntity("title2", "body2")

      TestRepository.insert(sampleEntity).futureValue
      TestRepository.insert(second).futureValue

      TestRepository.find(filter).futureValue mustBe Seq(sampleEntity)
    }

    "return empty sequence when no documents match" in {
      TestRepository.find(filter).futureValue mustBe Seq.empty[TestEntity]
    }

    "return all entities when filter is empty" in {
      val second = TestEntity("title2", "body2")

      TestRepository.insert(sampleEntity).futureValue
      TestRepository.insert(second).futureValue

      TestRepository.find().futureValue.toSet mustBe Set(sampleEntity, second)
    }

    "fail deserialization when projection removes required fields" in {
      TestRepository.insert(sampleEntity).futureValue

      recoverToSucceededIf[RuntimeException] {
        TestRepository.find(
          filter,
          projection = Some(exclude("body"))
        )
      }
    }

  }

  "replace" should {

    "replace an existing document" in {
      val updated = sampleEntity.copy(body = "updated-body")

      TestRepository.insert(sampleEntity).futureValue

      TestRepository.replace(filter, updated).futureValue mustBe true

      TestRepository.findOne(filter).futureValue mustBe Some(updated)
    }

    "upsert a document when none exists" in {
      val newEntity = TestEntity("new-title", "new-body")
      val newFilter = Filters.equal("title", newEntity.title)

      TestRepository.replace(newFilter, newEntity).futureValue mustBe true

      TestRepository.findOne(newFilter).futureValue mustBe Some(newEntity)
    }

    "not create duplicate documents when replacing" in {
      val updated = sampleEntity.copy(body = "updated")

      TestRepository.insert(sampleEntity).futureValue

      TestRepository.replace(filter, updated).futureValue mustBe true

      TestRepository.count(Filters.exists("title")).futureValue mustBe 1
    }

  }

  "delete" should {

    "remove a matching document and return deleted count" in {
      TestRepository.insert(sampleEntity).futureValue

      TestRepository.delete(filter).futureValue mustBe 1

      TestRepository.findOne(filter).futureValue mustBe None
    }

    "return 0 when no documents match" in {
      TestRepository.delete(filter).futureValue mustBe 0
    }

  }

  "count" should {

    "return 0 when no documents match" in {
      TestRepository.count(filter).futureValue mustBe 0
    }

    "return correct number of matching documents" in {
      val second = TestEntity("title2", "body2")

      TestRepository.insert(sampleEntity).futureValue
      TestRepository.insert(second).futureValue

      TestRepository.count(Filters.exists("title")).futureValue mustBe 2
    }

  }

  object TestRepository extends BaseMongoRepository[TestEntity](
    mongoComponent = application.injector.instanceOf[MongoComponent],
    collectionName = "test-collection"
  )

  case class TestEntity(title: String, body: String)

  object TestEntity {
    implicit val format: OFormat[TestEntity] = Json.format[TestEntity]
  }

  private lazy val sampleEntity = TestEntity("title", "body")
  private lazy val filter: Bson = Filters.equal("title", sampleEntity.title)

}
