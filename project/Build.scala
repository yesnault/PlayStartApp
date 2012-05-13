import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "Play20StartApp"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      "com.typesafe" %% "play-plugins-mailer" % "2.0.2"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here
      resolvers += "Apache" at "http://repo1.maven.org/maven2/"
    )

}
