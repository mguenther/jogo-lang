package com.mgu.jogo.interpreter;

/**
 * Interface for Turtle graphics provider on an Eucliden plane. Inspired by the
 * Turtle implementation that was part of the original Logo programming language
 * developed in 1966.
 *
 * @author Markus Günther <markus.guenther@gmail.com>
 */
public interface Turtle {

    /**
     * Clears all state this <code>Turtle</code> has generated during
     * the execution of previously called methods.
     */
    void clear();

    /**
     * Hides the turtle.
     */
    void hide();

    /**
     * Shows the turtle.
     */
    void show();

    /**
     * Disables drawing. Moving the turtle using <code>forward</code> or
     * <code>back</code> will not draw the turtle's path.
     */
    void penUp();

    /**
     * Enables drawing. Moving the turtle using <code>forward</code> or
     * <code>back</code> will draw the turtle's path.
     */
    void penDown();

    /**
     * Sets the pen color. Implementers of this interface must adhere
     * to the color definition of the original Logo programming language.
     * The following table sums it up.
     *
     * <table>
     *     <tr><th>Number</th><th>Color</th></tr>
     *     <tr><td>0</td><td>black</td></tr>
     *     <tr><td>1</td><td>blue</td></tr>
     *     <tr><td>2</td><td>green</td></tr>
     *     <tr><td>3</td><td>cyan</td></tr>
     *     <tr><td>4</td><td>red</td></tr>
     *     <tr><td>5</td><td>magenta</td></tr>
     *     <tr><td>6</td><td>yellow</td></tr>
     *     <tr><td>7</td><td>white</td></tr>
     *     <tr><td>8</td><td>brown</td></tr>
     *     <tr><td>9</td><td>tan</td></tr>
     *     <tr><td>10</td><td>forest</td></tr>
     *     <tr><td>11</td><td>aqua</td></tr>
     *     <tr><td>12</td><td>salmon</td></tr>
     *     <tr><td>13</td><td>violet</td></tr>
     *     <tr><td>14</td><td>orange</td></tr>
     *     <tr><td>15</td><td>grey</td></tr>
     * </table>
     *
     * @param penColor
     *      The pen color represented as a <code>int</code>
     */
    void setPenColor(int penColor);

    /**
     * Turns the turtle by <code>degrees</code> to the left.
     *
     * @param degrees
     *      <code>Integer</code> value representing the degrees
     *      this <code>Turtle</code> has to turn to the left
     */
    void left(int degrees);

    /**
     * Turns the turtle by <code>degrees</code> to the right.
     *
     * @param degrees
     *      <code>Integer</code> value representing the degrees
     *      this <code>Turtle</code> has to turn the right
     */
    void right(int degrees);

    /**
     * Moves the turtle <code>steps</code> backwards.
     *
     * @param steps
     *      <code>Integer</code> value representing the amount of steps
     *      this <code>Turtle</code> has to move backwards
     */
    void back(int steps);

    /**
     * Moves the turtle <code>steps</code> forward.
     *
     * @param steps
     *      <code>Integer</code> value representing the amount of steps
     *      this <code>Turtle</code> has to move forward
     */
    void forward(int steps);

    /**
     * Moves the turtle to an exact location encoded by the given location
     * parameters <code>positionX</code> and <code>positionY</code>.
     * This operation must not affect any graphical representation of
     * the turtle.
     *
     * @param positionX
     *      <code>Integer</code> that represents the abscissa (X-value)
     *      of the Turtle in an Eucliden plane
     * @param positionY
     *      <code>Integer</code> that represents the ordinate (Y-value)
     *      of the Turtle in an Euclidean plane
     */
    void move(int positionX, int positionY);
}
