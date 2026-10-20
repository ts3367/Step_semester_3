package abstraction_interface.assignment_problems;

// P1. Basic Drawing Canvas
// Shape is abstract -> new Shape() cannot compile.
// The two scale() overloads live on Shape itself and work for every subclass.
public class P1DrawingCanvas {

    public abstract static class Shape {
        // Shared static counter: incremented once inside Shape's own constructor,
        // every subclass reaches it automatically through super().
        private static int shapeCounter = 1000;

        private final String shapeId;

        // Scale multipliers kept on the base class so both scale() overloads can
        // stay concrete and be inherited unchanged by every subclass.
        private double xScale = 1.0;
        private double yScale = 1.0;

        protected Shape() {
            shapeCounter++;
            this.shapeId = "SH-" + shapeCounter;
        }

        public abstract double calculateArea();

        // One-argument overload: grows the shape equally.
        public void scale(double factor) {
            scale(factor, factor);
        }

        // Two-argument overload: grows it unevenly.
        // Same name, different parameter list -> compile-time polymorphism.
        public void scale(double xFactor, double yFactor) {
            this.xScale *= xFactor;
            this.yScale *= yFactor;
        }

        public String getShapeId() {
            return shapeId;
        }

        protected double getXScale() {
            return xScale;
        }

        protected double getYScale() {
            return yScale;
        }
    }

    public static class CircleShape extends Shape {
        private final double radius;

        public CircleShape(double radius) {
            this.radius = radius;
        }

        public double getRadius() {
            return radius;
        }

        @Override
        public double calculateArea() {
            // Scaling grows the area by the product of the two factors.
            return Math.PI * radius * radius * getXScale() * getYScale();
        }
    }

    public static class SquareShape extends Shape {
        private final double side;

        public SquareShape(double side) {
            this.side = side;
        }

        public double getSide() {
            return side;
        }

        @Override
        public double calculateArea() {
            return side * side * getXScale() * getYScale();
        }
    }

    // Prints the area without checking which subclass it actually is.
    public static void printArea(Shape s) {
        System.out.println("Area: " + s.calculateArea());
    }

    public static void main(String[] args) {
        CircleShape c = new CircleShape(5.0);
        System.out.println(c.calculateArea());   // ~78.53981633974483
        System.out.println(c.getShapeId());       // SH-1001

        SquareShape sq = new SquareShape(4.0);
        System.out.println(sq.calculateArea());  // 16.0

        sq.scale(2.0); // resolves to the one-argument overload at compile time
        System.out.println(sq.calculateArea());  // 64.0

        // Uneven growth resolves to the two-argument overload.
        sq.scale(3.0, 1.0);
        System.out.println(sq.calculateArea());  // 192.0

        printArea(c);
        printArea(sq);
    }
}
