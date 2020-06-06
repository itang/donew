// build.sc
import mill._, scalalib._
import coursier.maven.MavenRepository

object donew_cli extends ScalaModule {
  def scalaVersion = "2.12.11"

  def repositories = super.repositories ++ Seq(
    MavenRepository("http://maven.aliyun.com/nexus/content/groups/public")
  )
}
