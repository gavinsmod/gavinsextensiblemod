package com.peasenet.extensions

import org.joml.Vector3f

fun Vector3f.Vector3f(x: Double, y: Double, z: Double) {
    this.x = x.toFloat()
    this.y = y.toFloat()
    this.z = z.toFloat()
}