package com.chensiwen.edugame.particle.factory;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.chensiwen.edugame.particle.particle.Particle;

/**
 * Created by Administrator on 2015/11/29 0029.
 */
public abstract class ParticleFactory {
    public abstract Particle[][] generateParticles(Bitmap bitmap, Rect bound);
}
