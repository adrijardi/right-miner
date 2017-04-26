import sbt.Keys.name

scalaVersion := "2.12.2"

lazy val commonSettings = Seq(
  organization := "com.coding42",
  scalaVersion := "2.12.2"
)

val libVersion = new {
  val lwjglVersion = "3.1.1"
}


val engineLibraryDependencies = Seq(
  "org.lwjgl" % "lwjgl" % libVersion.lwjglVersion,
  "org.lwjgl" % "lwjgl-assimp" % libVersion.lwjglVersion,
  "org.lwjgl" % "lwjgl-bgfx" % libVersion.lwjglVersion,
  "org.lwjgl" % "lwjgl-egl" % libVersion.lwjglVersion,
  "org.lwjgl" % "lwjgl-glfw" % libVersion.lwjglVersion,
  "org.lwjgl" % "lwjgl-jawt" % libVersion.lwjglVersion,
  "org.lwjgl" % "lwjgl-jemalloc" % libVersion.lwjglVersion,
  "org.lwjgl" % "lwjgl-lmdb" % libVersion.lwjglVersion,
  "org.lwjgl" % "lwjgl-nanovg" % libVersion.lwjglVersion,
  "org.lwjgl" % "lwjgl-nfd" % libVersion.lwjglVersion,
  "org.lwjgl" % "lwjgl-nuklear" % libVersion.lwjglVersion,
  "org.lwjgl" % "lwjgl-openal" % libVersion.lwjglVersion,
  "org.lwjgl" % "lwjgl-opencl" % libVersion.lwjglVersion,
  "org.lwjgl" % "lwjgl-opengl" % libVersion.lwjglVersion,
  "org.lwjgl" % "lwjgl-opengles" % libVersion.lwjglVersion,
  //  "org.lwjgl" % "lwjgl-ovr" % version.lwjglVersion,
  "org.lwjgl" % "lwjgl-par" % libVersion.lwjglVersion,
  "org.lwjgl" % "lwjgl-sse" % libVersion.lwjglVersion,
  "org.lwjgl" % "lwjgl-stb" % libVersion.lwjglVersion,
  "org.lwjgl" % "lwjgl-tinyfd" % libVersion.lwjglVersion,
  "org.lwjgl" % "lwjgl-vulkan" % libVersion.lwjglVersion,
  "org.lwjgl" % "lwjgl-xxhash" % libVersion.lwjglVersion,
  "org.lwjgl" % "lwjgl" % libVersion.lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-assimp" % libVersion.lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-bgfx" % libVersion.lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-glfw" % libVersion.lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-jemalloc" % libVersion.lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-lmdb" % libVersion.lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-nanovg" % libVersion.lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-nfd" % libVersion.lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-nuklear" % libVersion.lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-openal" % libVersion.lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-opengl" % libVersion.lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-opengles" % libVersion.lwjglVersion classifier env,
  //  "org.lwjgl" % "lwjgl-ovr" % version.lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-par" % libVersion.lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-sse" % libVersion.lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-stb" % libVersion.lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-tinyfd" % libVersion.lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-xxhash" % libVersion.lwjglVersion classifier env
)

lazy val engine = (project in file("engine"))
  .settings(
    commonSettings,
    name := "game-engine",
    version := "0.1-SNAPSHOT",
    libraryDependencies ++= engineLibraryDependencies
  )

lazy val rightMiner = (project in file("right-miner"))
  .settings(
    commonSettings,
    name := "right-miner",
    version := "0.1-SNAPSHOT"
  )
  .dependsOn(engine)

lazy val root = (project in file("."))
  .aggregate(engine, rightMiner)
