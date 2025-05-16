    package org.example.model.shape;

    import javafx.scene.paint.Color;
    import javafx.scene.shape.Rectangle;

    public class RectangleShape extends Rectangle implements ShapeInterface {

        private Color fillColor;
        private Color strokeColor;

        // Constructor for rectangle with user input for fillColor and strokeColor
        //  If the user doesn't input filling color or stroke color then null will be passed to the constructor
        public RectangleShape(Color fillColor, Color strokeColor, double width, double height) {
            super(width, height);
            this.fillColor = (fillColor != null) ? fillColor : Color.GRAY;
            this.strokeColor = (strokeColor != null) ? strokeColor : Color.GRAY;
        }

        @Override
        public void draw(double X, double Y) {
            this.setStroke(this.getStrokeColor());
            this.setFill(this.getStrokeColor());
            this.setX(X);
            this.setY(Y);
        }

        @Override
        public void draw(Double[] points) {
            // TODO
        }

        public Color getFillColor() {
            return fillColor;
        }

        public void setFillColor(Color fillColor) {
            this.fillColor = fillColor;
        }

        public Color getStrokeColor() {
            return strokeColor;
        }

        public void setStrokeColor(Color strokeColor) {
            this.strokeColor = strokeColor;
        }

        @Override
        public double getHeightShape() {
            return this.getHeight();
        }

        @Override
        public double getWidthShape() {
            return this.getWidth();
        }

        @Override
        public void setSize(double width, double height) {
            this.setWidth(width);
            this.setHeight(height);
        }

        @Override
        public String toString() {
            return "Rectangle" + ";" + Color.web(this.getStrokeColor().toString()) + ";" + Color.web(this.getFillColor().toString()) +
                    ";" + this.getWidth() + ";" + this.getHeight() + ";" + this.getX() + ";" + this.getY() + "\n";
        }


    }
