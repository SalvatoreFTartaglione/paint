    package org.example.model.shape;

    import javafx.scene.paint.Color;
    import javafx.scene.shape.Rectangle;

    public class RectangleShape extends Rectangle implements ShapeInterface {

        private Color fillColor;
        private Color strokeColor;
        private int strokeWidth;


        public RectangleShape(Color strokeColor, Color fillColor, double width, double height, int strokeWidth) {
            super(width, height);
            this.strokeColor = strokeColor;
            this.fillColor = fillColor;
            this.strokeWidth = strokeWidth;
        }

        @Override
        public void draw(double X, double Y) {
            this.setStroke(this.strokeColor);
            this.setFill(this.fillColor);
            this.setStrokeWidth(this.strokeWidth);
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
        public void setSize(double width, double height) {
            this.setWidth(width);
            this.setHeight(height);
        }

        @Override
        public int getCustomStrokeWidth() {
            return strokeWidth;
        }

        @Override
        public void setStrokeWidth(int strokeWidth) {
            this.strokeWidth = strokeWidth;
        }

        @Override
        public String toString() {
            return "Rectangle" + ";" + this.getStrokeColor() + ";" + this.getFillColor() + ";" + this.getCustomStrokeWidth() +
                    ";" + this.getWidth() + ";" + this.getHeight() + ";" + this.getX() + ";" + this.getY() + "\n";
        }


    }
