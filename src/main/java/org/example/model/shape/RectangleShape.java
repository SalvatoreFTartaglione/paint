    package org.example.model.shape;

    import javafx.scene.Node;
    import javafx.scene.paint.Color;
    import javafx.scene.shape.Rectangle;

    public class RectangleShape extends Rectangle implements ShapeInterface {

        private Color fillColor;
        private Color strokeColor;
        private int strokeWidth;

        /**
         * Creates a new {@code RectangleShape} with the specified properties.
         *
         * @param strokeColor the border color
         * @param fillColor the fill color
         * @param width the width of the rectangle
         * @param height the height of the rectangle
         * @param strokeWidth the border thickness
         */
        public RectangleShape(Color strokeColor, Color fillColor, double width, double height, int strokeWidth) {
            super(width, height);
            this.strokeColor = strokeColor;
            this.fillColor = fillColor;
            this.strokeWidth = strokeWidth;
        }

        /**
         * Draws the rectangle at the specified position, applying stroke and fill properties.
         *
         * @param X the X coordinate of the top-left corner
         * @param Y the Y coordinate of the top-left corner
         */
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

        /**
         * @return the fill color of the rectangle
         */
        public Color getFillColor() {
            return fillColor;
        }

        /**
         * @return the JavaFX node representing this rectangle
         */
        @Override
        public Node getNode() {
            return this;
        }

        /**
         * Sets the fill color and updates the visual appearance.
         *
         * @param fill the new fill color
         */
        @Override
        public void setFillColor(Color fill) {
            this.fillColor = fill;
            this.setFill(fill);  // aggiorna graficamente la figura
        }

        /**
         * @return the stroke (border) color of the rectangle
         */
        public Color getStrokeColor() {
            return strokeColor;
        }

        /**
         * Sets the stroke color and updates the visual appearance.
         *
         * @param color the new stroke color
         */
        @Override
        public void setStrokeColor(Color color) {
            this.strokeColor = color;
            this.setStroke(color); // aggiorna visivamente
        }

        /**
         * Sets the width and height of the rectangle.
         *
         * @param width the new width
         * @param height the new height
         */
        @Override
        public void setSize(double width, double height) {
            this.setWidth(width);
            this.setHeight(height);
        }

        /**
         * @return the custom stroke width of the rectangle
         */
        @Override
        public int getCustomStrokeWidth() {
            return strokeWidth;
        }

        /**
         * Sets the stroke width (border thickness) of the rectangle.
         *
         * @param strokeWidth the new stroke width
         */
        @Override
        public void setStrokeWidth(int strokeWidth) {
            this.strokeWidth = strokeWidth;
        }

        /**
         * Serializes the rectangle to a string representation.
         * Format: Rectangle;strokeColor;fillColor;strokeWidth;width;height;x;y
         *
         * @return a semicolon-separated string representing the shape
         */
        @Override
        public String toString() {
            return "Rectangle" + ";" + this.getStrokeColor() + ";" + this.getFillColor() + ";" + this.getCustomStrokeWidth() +
                    ";" + this.getWidth() + ";" + this.getHeight() + ";" + this.getX() + ";" + this.getY() + "\n";
        }


    }
