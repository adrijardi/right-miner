import sbt.Keys.name

lazy val commonSettings = Seq(
  organization := "com.coding42",
  scalaVersion := "2.12.2"
)


val engineLibraryDependencies = Seq(
  "org.lwjgl" % "lwjgl" % lwjglVersion,
  "org.lwjgl" % "lwjgl-assimp" % lwjglVersion,
  "org.lwjgl" % "lwjgl-bgfx" % lwjglVersion,
  "org.lwjgl" % "lwjgl-egl" % lwjglVersion,
  "org.lwjgl" % "lwjgl-glfw" % lwjglVersion,
  "org.lwjgl" % "lwjgl-jawt" % lwjglVersion,
  "org.lwjgl" % "lwjgl-jemalloc" % lwjglVersion,
  "org.lwjgl" % "lwjgl-lmdb" % lwjglVersion,
  "org.lwjgl" % "lwjgl-nanovg" % lwjglVersion,
  "org.lwjgl" % "lwjgl-nfd" % lwjglVersion,
  "org.lwjgl" % "lwjgl-nuklear" % lwjglVersion,
  "org.lwjgl" % "lwjgl-openal" % lwjglVersion,
  "org.lwjgl" % "lwjgl-opencl" % lwjglVersion,
  "org.lwjgl" % "lwjgl-opengl" % lwjglVersion,
  "org.lwjgl" % "lwjgl-opengles" % lwjglVersion,
  //  "org.lwjgl" % "lwjgl-ovr" % lwjglVersion,
  "org.lwjgl" % "lwjgl-par" % lwjglVersion,
  "org.lwjgl" % "lwjgl-sse" % lwjglVersion,
  "org.lwjgl" % "lwjgl-stb" % lwjglVersion,
  "org.lwjgl" % "lwjgl-tinyfd" % lwjglVersion,
  "org.lwjgl" % "lwjgl-vulkan" % lwjglVersion,
  "org.lwjgl" % "lwjgl-xxhash" % lwjglVersion,
  "org.lwjgl" % "lwjgl" % lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-assimp" % lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-bgfx" % lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-glfw" % lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-jemalloc" % lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-lmdb" % lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-nanovg" % lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-nfd" % lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-nuklear" % lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-openal" % lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-opengl" % lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-opengles" % lwjglVersion classifier env,
  //  "org.lwjgl" % "lwjgl-ovr" % lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-par" % lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-sse" % lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-stb" % lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-tinyfd" % lwjglVersion classifier env,
  "org.lwjgl" % "lwjgl-xxhash" % lwjglVersion classifier env
)

lazy val engine = (project in file("engine"))
  .settings(
    commonSettings,
    name := "game-engine",
    version := "0.1-SNAPSHOT",
    libraryDependencies := engineLibraryDependencies
  )

lazy val rightMiner = (project in file("right-miner"))
  .settings(
    commonSettings,
    name := "right-miner",
    version := "0.1-SNAPSHOT"
  )
  .dependsOn(engine)

val lwjglVersion = "3.1.1"
