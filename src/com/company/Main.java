package com.company;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Point3f> list = new ArrayList<>();
        list.add(new Point3f(1f, 1f, 1f));
        list.add(new Point3f(0.3f, .8f, .9f));
        list.add(new Point3f(1.0f, 0f, 0.5f));
        list.add(new Point3f(.5f, 0f, 1.0f));
        list.add(new Point3f(.0f, 0.0f, 0.0f));
        CoordinateSystem system = new CoordinateSystem(list);
        system.addKeyListener(system);
    }
}
