package org.example.model.DTO;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DrawingContextDTOTest {

    private DrawingContextDTO dto;

    @BeforeEach
    public void setUp() {
        dto = new DrawingContextDTO();
    }

    @Test
    public void polygonIsNotReadyWithFewerThanFourPoints() {
        dto.setPolygonPoint(new Point2D(0, 0));
        dto.setPolygonPoint(new Point2D(100, 0));
        dto.setPolygonPoint(new Point2D(0, 0));
        assertFalse(dto.canCreatePolygon());
    }

    @Test
    public void polygonIsNotReadyUntilTheLastPointIsNearTheFirst() {
        dto.setPolygonPoint(new Point2D(0, 0));
        dto.setPolygonPoint(new Point2D(100, 0));
        dto.setPolygonPoint(new Point2D(100, 100));
        dto.setPolygonPoint(new Point2D(200, 200));
        assertFalse(dto.canCreatePolygon());
    }

    @Test
    public void polygonIsReadyWhenClosedWithinTolerance() {
        dto.setPolygonPoint(new Point2D(0, 0));
        dto.setPolygonPoint(new Point2D(100, 0));
        dto.setPolygonPoint(new Point2D(100, 100));
        dto.setPolygonPoint(new Point2D(10, 10));
        assertTrue(dto.canCreatePolygon());
    }

    @Test
    public void clearPolygonPointsEmptiesTheList() {
        dto.setPolygonPoint(new Point2D(1, 1));
        dto.clearPolygonPoints();
        assertTrue(dto.getPolygonPoints().isEmpty());
    }
}
