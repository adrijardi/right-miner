package com.coding42.engine.collision

import com.coding42.engine._
import util.isBetween

trait CollisionChecker[A <: Collider, B <: Collider] {
  def hasCollision(a: A, b: B)(world: World): Boolean
}

object CollisionChecker {

  object SphereSphereCollisionChecker extends CollisionChecker[SphereCollider, SphereCollider] {
    override def hasCollision(a: SphereCollider, b: SphereCollider)(world: World): Boolean = {
      val distance = Position.distance(a.position(world), b.position(world))
      distance < a.radius + b.radius
    }
  }

  object SphereBoxCollisionChecker extends CollisionChecker[SphereCollider, BoxCollider] {
    override def hasCollision(sphere: SphereCollider, box: BoxCollider)(world: World): Boolean = {
      var dmin = 0f

      val center = sphere.position(world)
      val bmin = box.position(world)
      val bmax = box.position(world) + box.size

      if (center.x < bmin.x) {
        dmin += Math.pow(center.x - bmin.x, 2).toFloat
      } else if (center.x > bmax.x) {
        dmin += Math.pow(center.x - bmax.x, 2).toFloat
      }

      if (center.y < bmin.y) {
        dmin += Math.pow(center.y - bmin.y, 2).toFloat
      } else if (center.y > bmax.y) {
        dmin += Math.pow(center.y - bmax.y, 2).toFloat
      }

      if (center.z < bmin.z) {
        dmin += Math.pow(center.z - bmin.z, 2).toFloat
      } else if (center.z > bmax.z) {
        dmin += Math.pow(center.z - bmax.z, 2).toFloat
      }

      dmin <= Math.pow(sphere.radius, 2)
    }
  }

  object BoxBoxCollisionChecker extends CollisionChecker[BoxCollider, BoxCollider] {
    override def hasCollision(a: BoxCollider, b: BoxCollider)(world: World): Boolean = {
      val posA = a.position(world)
      val posB = b.position(world)

      (isBetween(posA.x, posB.x, posB.x + b.size.x) || isBetween(posA.x + a.size.x, posB.x, posB.x + b.size.x)) &&
      (isBetween(posA.y, posB.y, posB.y + b.size.y) || isBetween(posA.y + a.size.y, posB.y, posB.y + b.size.y)) &&
      (isBetween(posA.z, posB.z, posB.z + b.size.z) || isBetween(posA.z + a.size.z, posB.z, posB.z + b.size.z))
    }
  }

  def hasCollision(a: Collider, b: Collider)(world: World): Boolean = {
    (a,b) match { // TODO horrible
      case (b1: SphereCollider, b2: SphereCollider) => SphereSphereCollisionChecker.hasCollision(b1, b2)(world: World)
      case (b1: BoxCollider, b2: BoxCollider) => BoxBoxCollisionChecker.hasCollision(b1, b2)(world: World)
      case (b1: SphereCollider, b2: BoxCollider) => SphereBoxCollisionChecker.hasCollision(b1, b2)(world: World)
      case (b1: BoxCollider, b2: SphereCollider) => SphereBoxCollisionChecker.hasCollision(b2, b1)(world: World)
    }
  }
}
