package uk.gov.nationalarchives.dp.client

import cats.MonadError
import uk.gov.nationalarchives.dp.client.Client.{BitStreamInfo, Entity}

import scala.xml.{Elem, NodeSeq}

class DataProcessor[F[_]]()(using me: MonadError[F, Throwable]) {

  extension (ns: NodeSeq)
    private def textOfFirstElement(): F[String] = ns.headOption.map(_.text) match
      case Some(value) => me.pure(value)
      case None        => me.raiseError(new RuntimeException("Generation not found"))

  def fragmentUrls(elem: Elem): F[Seq[String]] = {
    val fragments = elem \ "AdditionalInformation" \ "Metadata" \ "Fragment"
    me.pure(fragments.map(_.text))
  }

  def fragments(elems: Seq[Elem]): F[Seq[String]] = {
    val metadataObjects = elems.map(elem => {
      val eachContent: NodeSeq = elem \ "MetadataContainer" \ "Content"
      eachContent.flatMap(_.child).toString()
    })
    me.pure(metadataObjects.filter(!_.isBlank))
  }

  def generationUrlFromEntity(contentEntity: Elem): F[String] =
    (contentEntity \ "AdditionalInformation" \ "Generations").textOfFirstElement()

  def allGenerationUrls(entity: Elem): F[Seq[String]] =
    me.pure((entity \ "Generations" \ "Generation").map(_.text))

  def allBitstreamInfo(entity: Seq[Elem]): F[Seq[BitStreamInfo]] = {
    me.pure(
      entity.flatMap(e =>
        (e \ "Bitstreams" \ "Bitstream").map(b => {
          val name = b.attribute("filename").map(_.toString).getOrElse("")
          val url = b.text
          BitStreamInfo(name, url)
        })
      )
    )
  }

  def nextPage(elem: Elem): F[Option[String]] =
    me.pure((elem \ "Paging" \ "Next").headOption.map(_.text))

  def updatedEntities(elem: Elem): F[Seq[Entity]] =
    me.pure((elem \ "Entities" \ "Entity").map(e => {
      val entityAttributes = e.attributes
      Entity(
        entityAttributes.get("ref").map(_.toString).getOrElse(""),
        entityAttributes.get("title").map(_.toString).getOrElse(""),
        entityAttributes.get("type").map(_.toString).getOrElse(""),
        e.text,
        entityAttributes.get("deleted").getOrElse(Nil).nonEmpty
      )
    }))

}

object DataProcessor {
  def apply[F[_]]()(using me: MonadError[F, Throwable]) = new DataProcessor[F]()
}