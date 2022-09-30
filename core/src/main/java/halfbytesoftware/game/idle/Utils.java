package halfbytesoftware.game.idle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.Align;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {
    // random
    private static final Random s_random = new Random();
    public static int randomInt(int min, int max){
        return Utils.s_random.nextInt(max - min) + min;
    }
    public static long randomLong(long min, long max){
        return (long)(Math.random() * (max - min)) + min;
    }
    public static float randomFloat(float min, float max){
        return Utils.s_random.nextFloat() * (max - min) + min;
    }
    public static double randomDouble(){
        return Utils.s_random.nextDouble();
    }
    public static boolean randomBool(double chance){
        return randomDouble() <= chance;
    }
    public static boolean randomBool(float chance){
        return randomFloat(0.0f, 1.0f) <= chance;
    }
    public static boolean randomBool(){
        return randomBool(0.50f);
    }
    public static Color randomColor(){
        return new Color(Utils.randomFloat(0, 1), Utils.randomFloat(0, 1), Utils.randomFloat(0, 1), 1);
    }
    public static Vector2 randomVector2(float min, float max){
        float angle = Utils.randomFloat(0, (float)(2 * Math.PI));
        float mag = Utils.randomFloat(min, max);
        return new Vector2((float)(Math.sin(angle) * mag), (float)(Math.cos(angle) * mag));
    }

    // directions
    public static final List<GridPoint2> CARDINAL_DIRECTIONS = new ArrayList<GridPoint2>(){{
        add(new GridPoint2(0, -1));     // up
        add(new GridPoint2(1, 0));      // right
        add(new GridPoint2(0, 1));      // down
        add(new GridPoint2(-1, 0));     // left
    }};
    public static final List<GridPoint2> DIAGONAL_DIRECTIONS = new ArrayList<GridPoint2>(){{
        add(new GridPoint2(1, -1));     // up/right
        add(new GridPoint2(1, 1));      // down/right
        add(new GridPoint2(-1, 1));     // down/left
        add(new GridPoint2(-1, -1));    // up/eft
    }};
    public static final List<GridPoint2> ALL_DIRECTIONS = new ArrayList<GridPoint2>(){{
        addAll(CARDINAL_DIRECTIONS);
        addAll(DIAGONAL_DIRECTIONS);
    }};
    public enum EDirection{
        NONE, UP, RIGHT, DOWN, LEFT;
        public GridPoint2 toGridPoint2(){
            switch (this){
                case UP: return new GridPoint2(0, -1);
                case DOWN: return new GridPoint2(0, 1);
                case LEFT: return new GridPoint2(-1, 0);
                case RIGHT: return new GridPoint2(1, 0);
            }
            return new GridPoint2(0, 0);
        }
        public EDirection next(){
            // next ordinal not using 0 (NONE)
            int ordinal = Math.max(1, (this.ordinal() + 1) % EDirection.values().length);
            return EDirection.values()[ordinal];
        }
    }

    // ids
    private static long s_nextId = 0;
    public static long generateId(){
        return Utils.s_nextId++;
    }

    // color
    public static Color colorMultiply(Color color, float amount){
        color.r *= amount;
        color.g *= amount;
        color.b *= amount;
        return color;
    }
    public static Color colorAlpha(Color color, float alpha){
        color.a = alpha;
        return color;
    }
    public static boolean colorCompare(Color a, Color b){
        if (a.r == b.r && a.g == b.g && a.b == b.b && a.a == b.a){
            return true;
        }
        return false;
    }

    // distance
    public static float getEuclideanDistance(float x1, float y1, float x2, float y2){
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float)Math.sqrt(dx * dx + dy * dy);
    }
    public static Vector2 getDirection(float x1, float y1, float x2, float y2, boolean normalize){
        Vector2 dir = new Vector2((x2 - x1), (y2 - y1));
        if (normalize){
            dir = dir.nor();
        }
        return dir;
    }
    public static Vector2 getDirection(Actor a, Actor b, boolean normalize){
        Vector2 dir = new Vector2(b.getX(Align.center) - a.getX(Align.center), b.getY(Align.center) - a.getY(Align.center));
        if (normalize){
            dir = dir.nor();
        }
        return dir;
    }
    public static float getDistance(Actor a, Actor b){
        return Utils.getEuclideanDistance(a.getX(Align.center), a.getY(Align.center), b.getX(Align.center), b.getY(Align.center));
    }

    // collisions
    public static boolean circleRectangleCollision(float cx, float cy, float cr, float rx, float ry, float rw, float rh, float radiusMultiplier){
        // http://www.jeffreythompson.org/collision-detection/circle-rect.php

        // temporary variables to set edges for testing
        float testX = cx;
        float testY = cy;

        // which edge is closest?
        if (cx < rx){
            testX = rx;      // test left edge
        }
        else if (cx > rx + rw){
            testX = rx + rw;   // right edge
        }
        if (cy < ry){
            testY = ry;      // bottom edge
        }
        else if (cy > ry + rh){
            testY = ry + rh;   // top edge
        }

        // get distance from closest edges
        float distX = cx - testX;
        float distY = cy - testY;
        float distanceSquared = (distX * distX) + (distY * distY);

        // if the distance is less than the radius, collision!
        if (distanceSquared <= cr * cr * radiusMultiplier * radiusMultiplier) {
            return true;
        }
        return false;
    }
    public static boolean actorInRectangleCollision(Actor actor, Rectangle rect){
        return rect.contains(actor.getX(Align.center), actor.getY(Align.center));
    }

    // formatting
    /*
    public static String formatNumber(double value, int places){
        // power is based on places
        long power = (long) Math.pow(10, places);

        // whole number multiplied out
        long working = (long) (value * power);

        // compute whole part
        long whole = working / power;

        // compute fraction
        long fraction = working % power;

        // get rid of training zeros for the fraction
        while (fraction > 0 && fraction % 10 == 0){
            fraction /= 10;
        }

        // done
        if (fraction > 0){
            return "" + whole + "." + fraction;
        }
        else {
            return "" + whole;
        }
    }
    public static String formatNumber(long value, int places){
        // power is based on places
        long power = (long) Math.pow(10, places);

        // whole number multiplied out
        long working = value * power;

        // compute whole part
        long whole = working / power;

        // compute fraction
        long fraction = working % power;

        // get rid of training zeros for the fraction
        while (fraction > 0 && fraction % 10 == 0){
            fraction /= 10;
        }

        // done
        if (fraction > 0){
            return "" + whole + "." + fraction;
        }
        else {
            return "" + whole;
        }
    }
    public static String formatScientificNotation(long value){
        if (value < 1000){
            return "" + value;
        }
        else {
            NumberFormat numberFormat = new DecimalFormat("0.000E0");
            return numberFormat.format(value);
        }
    }
    public static String formatScientificNotation(double value){
        NumberFormat numberFormat = new DecimalFormat("0.000E0");
        return numberFormat.format(value);
    }
    private static final Format s_format = new DecimalFormat("0.0E0");
    public static String formatNumber(double value){
        if (value < 1000){
            return formatNumber(value, 0);
        }
        return s_format.format(value);
    }*/

    // misc
    public static void enableButton(Button button, boolean enable){
        button.setDisabled(!enable);
        button.setTouchable(enable ? Touchable.enabled : Touchable.disabled);
    }
}
