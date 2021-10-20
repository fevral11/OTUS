package ru.ognivenko

import io.circe._
import io.circe.syntax._

import java.io.{File, PrintWriter}
import scala.io.Source

object Search {

  case class Country(
                      name: String,
                      capital: Option[String],
                      area: Double,
                      region: String
                    )

  def main(args: Array[String]): Unit = {
    //Получаем список стран
    val source = Source.fromURL("https://raw.githubusercontent.com/mledoze/countries/master/countries.json")
    val js = source.mkString.stripMargin
    //Закрываем поток чтения из файла
    source.close()
    //Создаем decoder, на основании которого будут создаваться объекты только с нужными нам полями
    implicit val countryDecoder: Decoder[Country] = (c: HCursor) => for {
      area <- c.downField("area").as[Double]
      name <- c.downField("name").downField("common").as[String]
      capital <- c.downField("capital").downN(0).as[Option[String]]
      region <- c.downField("region").as[String]
    } yield Country(name, capital, area, region)
    //Получаем список объектов-стран
    val doc = parser.decode[List[Country]](js)
    //Создаем правило сортировки для стран на основании площади
    implicit val countryOrdering: Ordering[Country] = Ordering.by[Country, Double](_.area).reverse
    //Сортируем страны по площади и выбираем первые 10 значений
    val countries = doc.getOrElse(Nil).filter(c => c.region == "Africa").sorted(countryOrdering).take(10)
    implicit val encodeFoo: Encoder[Country] = (c: Country) => Json.obj(
      ("name", Json.fromString(c.name)),
      ("area", Json.fromDoubleOrNull(c.area)),
      ("capital", Json.fromString(c.capital.orNull))
    )
    //Формируем результирующий json
    val result = Json.fromValues(countries.map(c => c.asJson)).noSpaces
    //Получаем имя файла для записи
    val config = Config.parseArgs(args.map(a => a.replace("\r\n", "")))
    val fileName = config.path + config.file + ".txt"
    //Записываем результат в файл
    val fileObject = new File(fileName)
    val printWriter = new PrintWriter(fileObject)
    printWriter.write(result)
    printWriter.close()
  }
}



