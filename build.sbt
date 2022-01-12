name := "aconcagua"

val ACONCAGUA_VERSION = "1.1.1"

val PRICE_VERSION    = ACONCAGUA_VERSION
val STD_LIST_VERSION = ACONCAGUA_VERSION

val MAIN_SCALA = "2.13.7"
val SCALA_3    = "3.1.0"
val ALL_SCALA  = Seq(SCALA_3)

val ZIO_EVENT_SOURCING_VERSION = "0.2.0"

val CALIBAN_VERSION = "0.9.4"

val GRPC_VERSION = "1.36.1"

val UZHTTP_VERSION = "0.2.7"

val ZIO_VERSION = "1.0.13"

val ZIO_ZMX_VERSION = "0.0.4+69-01a7e756-SNAPSHOT"

inThisBuild(
  List(
    organization             := "dev.palanga",
    homepage                 := Some(url("https://github.com/palanga/aconcagua")),
    licenses                 := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    Test / parallelExecution := false,
    scmInfo                  := Some(
      ScmInfo(
        url("https://github.com/palanga/aconcagua/"),
        "scm:git:git@github.com:palanga/aconcagua.git",
      )
    ),
    developers               := List(
      Developer(
        "palanga",
        "Andrés González",
        "a.gonzalez.terres@gmail.com",
        url("https://github.com/palanga"),
      )
    ),
    // publishTo := Some("Artifactory Realm" at "https://palanga.jfrog.io/artifactory/maven/"),
    // credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
  )
)

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")

lazy val root =
  (project in file("."))
    .enablePlugins(ScalaJSPlugin)
    .settings(
      publish / skip := true
    )
    .aggregate(
//      core,
//      graphql,
//      grpc,
//      examples,
      price,
      std_list,
    )

lazy val price =
  (project in file("price"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings)
    .settings(
      name           := "price",
      version        := PRICE_VERSION,
      libraryDependencies ++= Seq(
//        "dev.zio" %% "zio"          % ZIO_VERSION,
        "dev.zio" %% "zio-test"     % ZIO_VERSION % "test",
        "dev.zio" %% "zio-test-sbt" % ZIO_VERSION % "test",
      ),
      testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
//      Test / fork    := true,
//      run / fork     := true,
    )
    .dependsOn(
      std_list
    )

// lazy val core =
//   (project in file("core"))
//     .settings(commonSettings)
//     .settings(
//       name := "aconcagua-core",
//       version := ACONCAGUA_VERSION,
//       libraryDependencies ++= Seq(
//         "dev.zio"      %% "zio-zmx" % ZIO_ZMX_VERSION,
//         "org.polynote" %% "uzhttp"  % UZHTTP_VERSION,
//       ),
//       testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
//       fork in Test := true,
//       fork in run := true,
//     )

// lazy val graphql =
//   (project in file("graphql"))
//     .settings(commonSettings)
//     .settings(
//       name := "aconcagua-graphql",
//       version := ACONCAGUA_VERSION,
//       libraryDependencies ++= Seq(
//         "com.github.ghostdogpr" %% "caliban"        % CALIBAN_VERSION,
//         "com.github.ghostdogpr" %% "caliban-http4s" % CALIBAN_VERSION,
//       ),
//       testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
//       fork in Test := true,
//       fork in run := true,
//     )
//     .dependsOn(
//       core
//     )

// lazy val grpc =
//   (project in file("grpc"))
//     .settings(commonSettings)
//     .settings(
//       name := "aconcagua-grpc",
//       version := ACONCAGUA_VERSION,
//       libraryDependencies ++= Seq(
//         "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
//         "io.grpc"               % "grpc-netty"           % GRPC_VERSION,
//       ),
//       PB.targets in Compile := Seq(
//         scalapb.gen(grpc = true)          -> (sourceManaged in Compile).value,
//         scalapb.zio_grpc.ZioCodeGenerator -> (sourceManaged in Compile).value,
//       ),
//       testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
//       fork in Test := true,
//       fork in run := true,
//     )
//     .dependsOn(
//       core
//     )

// lazy val examples =
//   (project in file("examples"))
//     .settings(commonSettings)
//     .settings(
//       name := "examples",
//       libraryDependencies ++= Seq(
//         "dev.palanga"   %% "zio-event-sourcing-core"                   % ZIO_EVENT_SOURCING_VERSION,
//         "dev.palanga"   %% "zio-event-sourcing-journal-cassandra-json" % ZIO_EVENT_SOURCING_VERSION,
//         "ch.qos.logback" % "logback-classic"                           % "1.2.9",
//       ),
//       PB.targets in Compile := Seq(
//         scalapb.gen(grpc = true)          -> (sourceManaged in Compile).value,
//         scalapb.zio_grpc.ZioCodeGenerator -> (sourceManaged in Compile).value,
//       ),
//       fork in Test := true,
//       fork in run := true,
//       skip in publish := true,
//     )
//     .dependsOn(
//       graphql,
//       grpc,
//     )

lazy val std_list =
  (project in file("std/list"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings)
    .settings(
      name           := "std-list",
      version        := STD_LIST_VERSION,
      libraryDependencies ++= Seq(
        "dev.zio" %% "zio-test"     % ZIO_VERSION % "test",
        "dev.zio" %% "zio-test-sbt" % ZIO_VERSION % "test",
      ),
      testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
//      Test / fork    := true,
//      run / fork     := true,
    )

val commonSettings =
  Def.settings(
    scalaVersion       := SCALA_3,
    crossScalaVersions := ALL_SCALA,
    // libraryDependencies += compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    // resolvers += "Artifactory" at "https://palanga.jfrog.io/artifactory/maven/",
    resolvers += Resolver.sonatypeRepo("snapshots"),
    // scalacOptions ++= Seq(
    //   "-deprecation",
    //   "-encoding",
    //   "UTF-8",
    //   "-explaintypes",
    //   "-Yrangepos",
    //   "-feature",
    //   "-language:higherKinds",
    //   "-language:existentials",
    //   "-unchecked",
    //   "-Xlint:_,-type-parameter-shadow",
    //   //    "-Xfatal-warnings",
    //   "-Ywarn-numeric-widen",
    //   "-Ywarn-unused:patvars,-implicits",
    //   "-Ywarn-value-discard",
    //   //      "-Ymacro-annotations",
    // ) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
    //   case Some((2, 12)) =>
    //     Seq(
    //       "-Xsource:2.13",
    //       "-Yno-adapted-args",
    //       "-Ypartial-unification",
    //       "-Ywarn-extra-implicit",
    //       "-Ywarn-inaccessible",
    //       "-Ywarn-infer-any",
    //       "-Ywarn-nullary-override",
    //       "-Ywarn-nullary-unit",
    //       "-opt-inline-from:<source>",
    //       "-opt-warnings",
    //       "-opt:l:inline",
    //     )
    //   case _             => Nil
    // }),
    //    scalacOptions in Test --= Seq("-Xfatal-warnings"),
  )
