package com.mgu.jogo.ide;

import com.mgu.jogo.interpreter.Turtle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class JavaFxTurtle implements Turtle {

    private static final Map<Integer, Color> COLOR_MAP = new HashMap<>();

    static {
        COLOR_MAP.put(0, Color.BLACK);
        COLOR_MAP.put(1, Color.BLUE);
        COLOR_MAP.put(2, Color.GREEN);
        COLOR_MAP.put(3, Color.CYAN);
        COLOR_MAP.put(4, Color.RED);
        COLOR_MAP.put(5, Color.MAGENTA);
        COLOR_MAP.put(6, Color.YELLOW);
        COLOR_MAP.put(7, Color.WHITE);
        COLOR_MAP.put(8, Color.BROWN);
        COLOR_MAP.put(9, Color.TAN);
        COLOR_MAP.put(10, Color.FORESTGREEN); // forest != forestgreen?
        COLOR_MAP.put(11, Color.AQUA);
        COLOR_MAP.put(12, Color.SALMON);
        COLOR_MAP.put(13, Color.VIOLET);
        COLOR_MAP.put(14, Color.ORANGE);
        COLOR_MAP.put(15, Color.GREY);
    }

    private final GraphicsContext gc;

    private boolean pathVisible = true;

    private Color currentColor = Color.WHITE;

    private final int maxHeight;

    private final int maxWidth;

    private double posX = 0;

    private double posY = 0;

    private double angle = 0;

    public JavaFxTurtle(final GraphicsContext gc, final int maxHeight, final int maxWidth) {
        this.gc = gc;
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
        this.posX = maxWidth / 2;
        this.posY = maxHeight / 2;
    }

    @Override
    public void clear() {
        gc.setFill(Color.WHITE);
        gc.fill();
    }

    @Override
    public void hide() {
        // TODO (mgu): Implement
    }

    @Override
    public void show() {
        // TODO (mgu): Implement
    }

    @Override
    public void penUp() {
        this.pathVisible = false;
    }

    @Override
    public void penDown() {
        this.pathVisible = true;
    }

    @Override
    public void setPenColor(int penColor) {
        this.currentColor = COLOR_MAP.get(penColor);
        this.gc.setStroke(this.currentColor);
    }

    @Override
    public void left(int degrees) {
        right(-degrees);
    }

    @Override
    public void right(int degrees) {
        this.angle += degrees;
        if (this.angle > 360) {
            this.angle -= 360;
        }
    }

    @Override
    public void back(int steps) {
        forward(-steps);
    }

    @Override
    public void forward(int steps) {
        final double oldX = this.posX;
        final double oldY = this.posY;
        this.posX += Math.cos(this.angle / 180.0 * Math.PI) * steps;
        this.posY += Math.sin(this.angle / 180.0 * Math.PI) * steps;

        // TODO: Boundary checks

        if (this.pathVisible) {
            this.gc.strokeLine(oldX, oldY, this.posX, this.posY);
        }
    }

    @Override
    public void move(int positionX, int positionY) {
        this.posX = positionX;
        this.posY = positionY;
    }
}
