package com.example.dennis.leagueoflegions.gl;

import com.example.dennis.leagueoflegions.gl.shape.Circle;
import com.example.dennis.leagueoflegions.gl.shape.Point;
import com.example.dennis.leagueoflegions.model.GameObject;
import com.example.dennis.leagueoflegions.model.Projectile;

public class GLProjectile extends GLObject {
    private Point point;
    private Circle circle;

    public GLProjectile(Projectile projectile) {
        super(projectile);
        point = new Point(projectile.getOriginPlayer().getColor());
        circle = new Circle(projectile.getOriginPlayer().getColor());
    }

    @Override
    public void tick() {
        GameObject gameObject = getGameObject();
        point.setXYZ(gameObject.getX(), gameObject.getY(), 0f);
    }

    @Override
    public void draw(float[] mVPMatrix) {
        super.draw(mVPMatrix);
        point.draw(mVPMatrix);
        circle.draw(getMVPMatrix());
    }
}
