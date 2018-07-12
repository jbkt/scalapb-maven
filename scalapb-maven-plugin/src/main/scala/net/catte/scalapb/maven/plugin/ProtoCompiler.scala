package net.catte.scalapb.maven.plugin

import java.io.File
import java.nio.file.{Path, Paths}
import java.util.StringJoiner

import scala.util.matching.Regex
import scalapb.ScalaPBC

object ProtoCompiler {

  private def recursiveListPaths(f: File, r: Regex): Array[Path] = {
    val these = f.listFiles
    val good = these
      .filter(f => r.findFirstIn(f.getName).isDefined)
      .map(file => Paths.get(file.toURI))
    good ++ these.filter(_.isDirectory).flatMap(recursiveListPaths(_,r))
  }

  def compile(protocVersion: String,
              inputDirectoryPath: Path,
              includeDirectoriesPaths: Array[Path],
              outputDirectoryPath: Path,
              flatPackage: Boolean,
              javaConversions: Boolean,
              javaOutput: Boolean,
              javaOutputPath: Path): Unit = {
    val protoPathFile: File = new File(inputDirectoryPath.toUri)

    val files: Array[Path] = recursiveListPaths(protoPathFile, ".*\\.proto".r)

    val protoPathsArgs: Array[String] = (inputDirectoryPath +: includeDirectoriesPaths).map {
      path => "--proto_path=" + path
    }

    val scalaOutOptionsJoiner = new StringJoiner(",")
    if(flatPackage) {
      scalaOutOptionsJoiner.add("flat_package")
    }
    if(javaConversions) {
      scalaOutOptionsJoiner.add("java_conversions")
    }

    val scalaOutOptions = scalaOutOptionsJoiner.toString

    val scalaOutArgs = s"$scalaOutOptions:$outputDirectoryPath"

    val javaArgs = if(javaOutput) {
      Array(s"--java_out=$javaOutputPath")
    } else {
      Array.empty[String]
    }

    val args = Array[String](s"-$protocVersion", "--throw") ++
      protoPathsArgs ++
      javaArgs ++
      (
        s"--scala_out=$scalaOutArgs" +:
        files.map(_.toString)
      )

    ScalaPBC.main(args)
  }
}
