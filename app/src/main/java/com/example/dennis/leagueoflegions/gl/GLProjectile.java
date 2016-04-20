package com.example.dennis.leagueoflegions.gl;

import com.example.dennis.leagueoflegions.gl.shape.FilledCircle;
import com.example.dennis.leagueoflegions.model.Projectile;

public class GLProjectile extends GLObject {
    private FilledCircle circle;

    public GLProjectile(Projectile projectile) {
        super(projectile);
        circle = new FilledCircle(projectile.getOriginPlayer().getColor());
    }

    @Override
    public void tick() {
//        Projectile projectile = (Projectile) getGameObject();
    }

    @Override
    public void draw(float[] mVPMatrix) {
        super.draw(mVPMatrix);
        circle.draw(getMVPMatrix());
    }
}
