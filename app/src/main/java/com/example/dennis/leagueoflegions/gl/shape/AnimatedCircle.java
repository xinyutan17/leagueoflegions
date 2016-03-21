package com.example.dennis.leagueoflegions.gl.shape;

public class AnimatedCircle extends Circle {
    private static final float RADIUS_MIN = 0.9f;       // minimum radius value
    private static final float RADIUS_MAX = 1.1f;       // maximum radius value
    private static final float RADIUS_STEP = 0.01f;    // radius step at each update

    private float[] radiiStepDir;       // the direction of the radius step at each update

    public AnimatedCircle(float[] color) {
        super(color);

        radiiStepDir = new float[NUMBER_VERTICES];
        for (int i = 0; i < NUMBER_VERTICES; i++) {
            if (Math.random() < 0.5) {
                radiiStepDir[i] = 1;
            } else {
                radiiStepDir[i] = -1;
            }
        }
    }

    @Override
    public void tick() {
        float[] radii = getRadii();
        float[] vertices = getVertices();
        for (int i = 0; i < NUMBER_VERTICES; i++) {
            if (Math.random() < 0.33) {
                if (Math.random() < 0.1) {
                    radiiStepDir[i] *= -1;
                }
                radii[i] += radiiStepDir[i]*RADIUS_STEP;
                if (radii[i] < RADIUS_MIN) {
                    radii[i] = RADIUS_MIN;
                    radiiStepDir[i] = 1;
                } else if (radii[i] > RADIUS_MAX) {
                    radii[i] = RADIUS_MAX;
                    radiiStepDir[i] = -1;
                }
                vertices[3*(i+1)+0] = (float) (radii[i]*Math.cos(i*DELTA_ANGLE));
                vertices[3*(i+1)+1] = (float) (radii[i]*Math.sin(i*DELTA_ANGLE));
            }
        }
        vertices[3*(NUMBER_VERTICES+1)+0] = vertices[3*(0+1)+0];
        vertices[3*(NUMBER_VERTICES+1)+1] = vertices[3*(0+1)+1];
        setRadii(radii);
        setVertices(vertices);
    }
}
