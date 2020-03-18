package com.company;


import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.interpolators.*;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.vecmath.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

//
//3. Construct a Shape3D node with a TransformGroup node above it.
//4. Attach a RotationInterpolator behavior to the TransformGroup.
//5. Call the universe builder utility function to do the following:
//a. Establish a virtual universe with a single high-resolution Locale
//b. Create the PhysicalBody, PhysicalEnvironment, View, and ViewPlat-form objects.
//c. Create a BranchGroup as the root of the view platform branch graph.
//d. Insert the view platform branch graph into the Locale.
//6. Insert the scene branch graph into the universe builder's Locale.
public class CoordinateSystem extends Applet implements KeyListener{
    private static Point3d GazePoint = new Point3d(0,1,2);
    private static Point3d ViewerLocation = new Point3d(1,1,1);
    ArrayList<Point3f> coordinates;

    TransformGroup transformGroup = new TransformGroup();
    Transform3D transform = new Transform3D();

    private double xLoc = 0.0;
    private double yLoc = 0.0;
    private double zLoc = 0.0;

    public CoordinateSystem(ArrayList<Point3f> input){
        coordinates = input;
//1. Create a Canvas3D object and add it to the Applet panel.
        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        add(canvas);
        canvas.addKeyListener(this);

//2. Create a BranchGroup as the root of the scene branch graph.
        BranchGroup sceneRoot = createScene();



//5. Call the universe builder utility function to do the following:
        SimpleUniverse simpleUniverse = new SimpleUniverse(canvas);
        simpleUniverse.getViewingPlatform().setNominalViewingTransform();
        transform.lookAt(GazePoint, ViewerLocation, new Vector3d(2, 1, 0 ));
        transformGroup = simpleUniverse.getViewingPlatform().getMultiTransformGroup().getTransformGroup(0);
        //Bare så den ikke hoper ved rotation
        transform.invert();
        transformGroup.setTransform(transform);
//6. Insert the scene branch graph into the universe builder's Locale.
        simpleUniverse.addBranchGraph(sceneRoot);
        MainFrame frame = new MainFrame(this, 720, 480);
        frame.setTitle("Coordinate Test");
        frame.setVisible(true);
    }

    //2. Create a BranchGroup as the root of the scene branch graph.
    public BranchGroup createScene(){
        BranchGroup branchGroup = new BranchGroup();

        Background background = new Background(new Color3f(Color.black));
        BoundingSphere boundingSphere = new BoundingSphere(new Point3d(0,0,0), 1000);
        background.setApplicationBounds(boundingSphere);
        branchGroup.addChild(background);
        createLineArray(branchGroup);
        return branchGroup;
    }

    //3. Construct a Shape3D node with a TransformGroup node above it.
    public void createLineArray(BranchGroup branchGroup){
        for(int x = 0; x < coordinates.size()-1; x++) {
            LineArray lines = new LineArray(2, LineArray.COORDINATES);
            lines.setCoordinate(0, new Point3f(coordinates.get(x).getX(), coordinates.get(x).getY(), coordinates.get(x).getZ()));
            lines.setCoordinate(1, new Point3f(coordinates.get(x+1).getX(), coordinates.get(x+1).getY(), coordinates.get(x+1).getZ()));
            LineAttributes lineAttributes = new LineAttributes();
            lineAttributes.setLinePattern(LineAttributes.PATTERN_SOLID);
            lineAttributes.setLineWidth(5f);

            Appearance appearance = new Appearance();
            appearance.setLineAttributes(lineAttributes);
            ColoringAttributes coloringAttributes = new ColoringAttributes();
            coloringAttributes.setColor(new Color3f(Color.red));
            appearance.setColoringAttributes(coloringAttributes);
            Shape3D line = new Shape3D(lines, appearance);
            transformGroup.addChild(line);

;
        }

        //4. Attach a RotationInterpolator behavior to the TransformGroup.
        Transform3D yAxis = new Transform3D();
        Alpha timing = new Alpha(0, 0);
        RotationInterpolator nodeRotator = new RotationInterpolator(timing, transformGroup, yAxis, 0.0f, 0.0f);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 0),0);
        nodeRotator.setSchedulingBounds(bounds);
        transformGroup.addChild(nodeRotator);

        branchGroup.addChild(transformGroup);
    }

    private void rotate(){
        Transform3D delta = new Transform3D();
        Vector3d vector = new Vector3d(xLoc, yLoc, zLoc);
        transform.lookAt(GazePoint, ViewerLocation, new Vector3d( 2,1,0));
        delta.setEuler(vector);
        transform.mul(delta);
        transform.invert();
        transformGroup.setTransform(transform);
    }


    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode){
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                yLoc += 0.1;
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                yLoc -= 0.1;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                xLoc -= 0.1;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                xLoc += 0.1;
                break;
            case KeyEvent.VK_Q:
                zLoc -= 0.1;
                break;
            case KeyEvent.VK_E:
                zLoc += 0.1;
                break;
            case KeyEvent.VK_PLUS:
                GazePoint.y+=0.5;
                break;
            case KeyEvent.VK_MINUS:
                GazePoint.y-=0.5;
            default:
                break;
        }

        rotate();
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
