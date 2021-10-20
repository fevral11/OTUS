package ru.ognivenko

import scopt.{OParser, OParserBuilder}

case class Config(
                   file: String = "countries",
                   path: String = "C:\\"
                 )

object Config {
  val builder: OParserBuilder[Config] = OParser.builder[Config]

  val parser1: OParser[Unit, Config] = {
    import builder._
    OParser.sequence(
      programName("scopt"),
      head("scopt", "4.x"),
      opt[String]('f', "file")
        .action((v, c) => c.copy(file = v))
        .text("file is an string property"),
      opt[String]('p', "path")
        .action((v, c) => c.copy(file = v))
        .text("path is an string property")
    )
  }

  def parseArgs(args: Array[String]): Config =
    OParser
      .parse(parser1, args, Config())
      .getOrElse(sys.error("Could not parse arguments"))
}
