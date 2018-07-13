package net.catte.scalapb.maven.plugin

import java.io.File
import java.nio.file.{Path, Paths}
import java.util.StringJoiner

import scala.collection.mutable.ArrayBuffer
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

  private def recursiveListProtoFilesInInputDirectoryPath(path: Path): Array[Path] = {
    val file: File = new File(path.toUri)

    if(!file.exists()) {
      throw new IllegalArgumentException(s"inputDirectoryPath ($path) does not exist")
    }

    recursiveListPaths(file, ".*\\.proto".r)
  }

  def compile(protocVersion: String,
              inputDirectoryPath: Path,
              includeDirectoriesPaths: Array[Path],
              outputDirectoryPath: Path,
              flatPackage: Boolean,
              javaConversions: Boolean,
              javaOutput: Boolean,
              javaOutputPath: Path): Unit = {
    val files: Array[Path] = recursiveListProtoFilesInInputDirectoryPath(inputDirectoryPath)

    if (files.isEmpty) {
      throw new IllegalArgumentException(s"$inputDirectoryPath does not contain .proto files")
    }

    val protoPathsArgs: Array[String] = (inputDirectoryPath +: includeDirectoriesPaths).map {
      path => "--proto_path=" + path
    }

    val scalaOutOptionsJoiner = new StringJoiner(",")
    if (flatPackage) {
      scalaOutOptionsJoiner.add("flat_package")
    }
    if (javaOutput && javaConversions) {
      scalaOutOptionsJoiner.add("java_conversions")
    }

    val scalaOutOptions = scalaOutOptionsJoiner.toString

    val scalaOutArgs = s"$scalaOutOptions:$outputDirectoryPath"

    val javaArgs = if (javaOutput) {
      Array(s"--java_out=$javaOutputPath")
    } else {
      Array.empty[String]
    }

    val argsBuilder = new ArrayBuffer[String]()
    argsBuilder.append(s"-$protocVersion")
    argsBuilder.append("--throw")
    argsBuilder.append(protoPathsArgs: _*)
    argsBuilder.append(javaArgs: _*)
    argsBuilder.append(s"--scala_out=$scalaOutArgs")
    argsBuilder.append(files.map(_.toString): _*)

    val args = argsBuilder.toArray

    ScalaPBC.main(args)
  }
}
