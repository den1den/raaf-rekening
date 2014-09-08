/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.diplay.gui.graph;

import geld.Transactie;
import java.awt.Color;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Dennis
 */
public class DataSet<X, Y> {

    public static DataSet createFrom(List<Transactie.Record> list) {
        Class y = Transactie.Record.class;
        return histogram(y, list);
    }

    String titel;
    Color color;

    final private Translator<X> xTranslator;
    final private Translator<Y> yTranslator;

    final Class<X> classX;
    final Class<Y> classY;
    Point<X, Y> points[];

    double[] x;
    double[] y;

    final Range range = new Range();

    Renderer renderer;

    private DataSet(String titel, Color color, Translator<X> xTranslator,
            Translator<Y> yTranslator, Class<X> classX, Class<Y> classY,
            Point<X, Y>[] points, Renderer renderer) {
        this.titel = titel;
        this.color = color;
        this.xTranslator = xTranslator;
        this.yTranslator = yTranslator;
        this.classX = classX;
        this.classY = classY;
        this.points = points;
        this.x = new double[points.length];
        this.y = new double[points.length];
        this.renderer = renderer;
        setXY();
    }

    public static <X, Y> DataSet create(String titel,
            Color color,
            Class<X> classX,
            Iterable<X> valuesX,
            Class<Y> classY,
            Iterable<Y> valuesY,
            Renderer renderer) {

        if(color == null){
            color = new Color(
                    (float)Math.random(),
                    (float)Math.random(),
                    (float)Math.random()
            );
        }
        
        Iterator<X> itX = valuesX.iterator();
        Iterator<Y> itY = valuesY.iterator();
        List<Point> ps = new LinkedList<>();
        while (itX.hasNext() && itY.hasNext()) {
            Point p = new Point(itX.next(), itY.next());
            ps.add(p);
        }
        Point<X, Y> points[] = (Point<X, Y>[]) Array.newInstance(Point.class, ps.size());
        int i = 0;
        for (Iterator<Point> it = ps.iterator(); it.hasNext();) {
            points[i] = it.next();
            i++;
        }

        Translator t = Translator.getDefault(classX);
        if (t == null) {
            throw new IllegalArgumentException(classX + " class not translatable");
        }
        Translator<X> xTranslator = (Translator<X>) t;

        t = Translator.getDefault(classY);
        if (t == null) {
            throw new IllegalArgumentException(classY + " class not translatable");
        }
        Translator<Y> yTranslator = (Translator<Y>) t;
        return new DataSet(titel, color, xTranslator, yTranslator, classX, classY, points, renderer);
    }

    public static <Y> DataSet<Integer, Y> histogram(Class<Y> classY,
            List<Y> valuesY) {
        String titel;
        Color color;
        Class classX = Integer.class;
        Iterable<Integer> valuesX;
        Renderer renderer;

        titel = "Histogram van " + classY;
        color = Color.BLUE;
        renderer = new RendererLines();
        valuesX = createOplopend(valuesY.size());
        return create(titel, color, classX, valuesX, classY, valuesY, renderer);
    }

    public static DataSet[] getTest1() {
        String titel;
        Color color;
        Class classX = Integer.class;
        Iterable<Integer> valuesX;
        Class classY = Double.class;
        ArrayList<Double> valuesY;
        Renderer renderer;

        titel = "test1";
        color = Color.RED;
        renderer = new RendererLines();

        valuesX = createOplopend(3);
        valuesY = new ArrayList<>(3);
        valuesY.add(-1.);
        valuesY.add(1.);
        valuesY.add(0.);

        DataSet a = create(titel, color, classX, valuesX, classY, valuesY, renderer);

            //double A = (double)i/40;
        //double B = (A - 10) * 0.5;
        //double C = A / (B * (B + 5) + 10) * 100;
        DataSet c = fromFunction(11, 21, (double x1) -> x1 * x1);
        c.renderer = new RendererLines();
        DataSet d = fromFunction(11, 22, (double x1) -> x1 * x1);
        d.color = Color.BLUE;
        return new DataSet[]{a, c, d};
    }
    
    public static DataSet[] getTestSinus(){
        String titel;
        Color color;
        Class classX = Double.class;
        ArrayList<Double> valuesX;
        Class classY = Double.class;
        ArrayList<Double> valuesY;
        Renderer renderer;

        titel = "test Sinus";
        color = Color.RED;
        renderer = new RendererLines();

        int points = 1000;
        valuesX = new ArrayList<>(points);
        valuesY = new ArrayList<>(points);
        for (int i = 0; i < points; i++) {
            double x = (double)(i - points/2)/points * Math.PI;
            double y = Math.sin(x);
            valuesX.add(x);
            valuesY.add(y);
        }

        DataSet a = create(titel, color, classX, valuesX, classY, valuesY, renderer);
        return new DataSet[]{a};
    }
    
    public static DataSet[] getTest0(){
        String titel;
        Color color;
        Class classX = Date.class;
        ArrayList<Date> valuesX;
        Class classY = Double.class;
        ArrayList<Double> valuesY;
        Renderer renderer;

        titel = "test0";
        color = Color.RED;
        renderer = new RendererLines();

        valuesX = new ArrayList<>(3);
        Date today = new Date();
        valuesX.add(today);
        valuesX.add(new Date(today.getYear(), today.getMonth(), today.getDay()-1));
        valuesX.add(new Date(today.getYear(), today.getMonth(), today.getDay()-2));
        valuesX.add(new Date(today.getYear(), today.getMonth(), today.getDay()-3));
        valuesY = new ArrayList<>(3);
        valuesY.add(-1.);
        valuesY.add(1.);
        valuesY.add(0.);
        valuesY.add(0.5);

        DataSet a = create(titel, color, classX, valuesX, classY, valuesY, renderer);
        return new DataSet[]{a};
    }

    public interface Function {

        double f(double x);
    }

    public static DataSet fromFunction(double range, int samples, Function f) {
        String titel;
        Color color;
        Class classX = Double.class;
        ArrayList<Double> valuesX = new ArrayList<>(samples);
        Class classY = Double.class;
        ArrayList<Double> valuesY = new ArrayList<>(samples);
        Renderer renderer = new RendererPoints();

        titel = "From function";
        color = Color.BLACK;

        double step = range / samples;
        for (int i = 0; i < samples; i++) {
            double x = step * (i - samples / 2);
            double y = f.f(x);
            valuesX.add(x);
            valuesY.add(y);
        }
        return create(titel, color, classX, valuesX, classY, valuesY, renderer);
    }

    private static Iterable<Integer> createOplopend(final int length) {
        ArrayList<Integer> is = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            is.add(i);
        }
        return is;
    }

    private void setXY() {
        for (int i = 0; i < points.length; i++) {
            Point<X, Y> p = points[i];
            double px = xTranslator.translate(p.xValue);
            double py = yTranslator.translate(p.yValue);
            range.miximizaPoint(px, py);
            x[i] = px;
            y[i] = py;
        }
    }

    public void add(X newW, Y newY) {
        double xL = xTranslator.translate(newW);
        double yL = yTranslator.translate(newY);
        range.miximizaPoint(xL, yL);
        Point<X, Y> p = new Point(newW, newY);
        Point[] points2 = new Point[points.length];
        System.arraycopy(points, 0, points2, 0, points2.length);
        points2[points.length] = p;
        double[] x2 = add(x, xL);
        double[] y2 = add(y, yL);

        this.points = points2;
        this.x = x2;
        this.y = y2;
    }

    static double[] add(double[] ls, double l) {
        double[] ls2 = new double[ls.length + 1];
        System.arraycopy(ls, 0, ls2, 0, ls.length);
        ls2[ls.length] = l;
        return ls2;
    }

    int size() {
        return this.points.length;
    }
}
