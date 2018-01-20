package net.catte.scalapb.maven.plugin

import java.nio.file.{Files, Path}

import com.trueaccord.scalapb.ScalaPBC

import scala.collection.JavaConverters._

object ProtoCompiler {

  def compile(protoPath: Path, scalaOut: Path): Unit = {

    val files: Array[String] = Files.list(protoPath).iterator().asScala.map(_.toString).toArray

    val args: Array[String] = Array[String](
      "-v300",
      "--throw",
      s"--proto_path=$protoPath",
      s"--scala_out=$scalaOut") ++ files

    ScalaPBC.main(args)
  }
}
