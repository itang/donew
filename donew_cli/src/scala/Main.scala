package donew_cli

import java.io.File
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Main {
  private val DefaultDenoProjectName = "deno_test_"
  private val DefaultGoProjectName = "go_test_"

  def main(args: Array[String]): Unit = args match {
    case Array("deno")       => _donewDeno(DefaultDenoProjectName + dateStr())
    case Array("deno", name) => _donewDeno(name)
    case Array("go")         => _donewGo(DefaultGoProjectName + dateStr())
    case Array("go", name)   => _donewGo(name)
    case Array("invoke")     => _donewInvoke()
    case _                   => println("any?")
  }

  private def _donewDeno(name: String, args: String*): Unit = {
    val root = new java.io.File(name)

    println(s"mkdir ${name}...")
    val _ = root.mkdir()

    println(s"cd ${name}; denoinit")
    val r = sys.process.Process(Seq("denoinit"), root).!!
    println(r)

    val rakeFileContent = s"""task default: :usage

    task :usage do
      sh "rake -T"
    end

    desc "build"
    task :build do
      sh "mkdir dist && deno compile --unstable main.ts -o dist/${name}"
    end
   """
    Files.write(
      new File(root, "Rakefile").toPath,
      rakeFileContent.getBytes("UTF-8")
    )

    println(s"cd ${name} for your work!")
  }

  private def _donewGo(name: String, args: String*): Unit = {
    new GoProjectMaker(name, args).gen()
  }

  def _donewInvoke(): Unit = {
    val d="\"\"\""
    val s =
      s"""|from invoke import task
         |
         |
         |@task
         |def version(c, cmd=None):
         |    "println version"
         |    cmd = 'invoke' if cmd == None else cmd
         |    v = "-version" if cmd == 'java' else '--version'
         |    c.run('{} {}'.format(cmd, v))
         |
         |
         |@task(default=True)
         |def usage(c):
         |    ${d}Usage${d}
         |    c.run('invoke -l')
         |
         |""".stripMargin
    Files.write(new File("tasks.py").toPath, s.getBytes("UTF-8"))
  }

  private def dateStr() =
    DateTimeFormatter.ofPattern("yyyyMMddHHmm").format(LocalDateTime.now())
}

//TODO:
trait ProjectMaker {
  def gen(): Unit
}

abstract class AbstractProjectMaker extends ProjectMaker {
  override def gen(): Unit = {
    println(s"mkdir ${name}...")
    val ok = root.mkdir()
    println(s"mkdir ${name} ok?: ${ok}")
    doWithShellInProjectDir()
    doWithWriteFiles()

    println(s"cd ${name} for your work!")
  }

  private def doWithShellInProjectDir(): Unit = {
    for (cmd <- getShells()) {
      val r = sys.process.Process(cmd, root).!!
      println(r)
    }
  }

  private def doWithWriteFiles() {
    import java.nio.file.Files
    for (FileWithContent(path, content) <- getFileWithContent()) {
      println(s"write ${path}...")
      Files.write(new File(root, path).toPath(), content.getBytes("utf-8"));
    }
  }

  def root = new File(name)

  def name: String

  def args: Seq[String]

  def getShells(): Seq[Seq[String]]

  def getFileWithContent(): Seq[FileWithContent]
}

case class FileWithContent(path: String, content: String)
class GoProjectMaker(val name: String, val args: Seq[String])
    extends AbstractProjectMaker {
  override def getShells =
    Seq(Seq("go", "mod", "init", s"github.com/itang/${name}"))

  override def getFileWithContent() =
    Seq(FileWithContent("main.go", s"""package main

import "fmt"

func main(){
    fmt.Println("Hello, World!")
}
"""))
}
