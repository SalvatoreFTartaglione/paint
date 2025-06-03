package org.example.model.concreteCommands;

import javafx.geometry.Point2D;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.commandInterface.CommandInterface;
import org.example.model.receivers.PositionManagerReceiver;
import org.example.model.enums.MirrorDirectionEnum;

import java.util.Optional;

/**
 * Represents a command for mirroring a shape in a specific direction.
 * This class implements the Command design pattern and provides functionality
 * to execute, undo, and simulate a mirroring operation based on a given direction.
 *
 * The mirroring operation is applied on the provided {@code PositionManagerReceiver},
 * which handles the details of manipulating the underlying shape.
 */
public class MirroringCommand implements CommandInterface {

    private PositionManagerReceiver positionManagerReceiver;
    private MirrorDirectionEnum mirrorDirection;
    private DrawingContextDTO contextDTO;

    /**
     * Constructs a new instance of the MirroringCommand, which performs a mirroring
     * operation on a shape using a specified mirroring direction and drawing context.
     *
     * @param positionManagerReceiver the receiver that handles position-related operations for the shape
     * @param mirrorDirection the direction in which the shape should*/
    public MirroringCommand(PositionManagerReceiver positionManagerReceiver, MirrorDirectionEnum mirrorDirection, DrawingContextDTO contextDTO) {
        this.positionManagerReceiver = positionManagerReceiver;
        this.mirrorDirection = mirrorDirection;
        this.contextDTO = contextDTO;
    }

    /**
     * Executes the mirroring operation based on the specified direction.
     * Delegates the mirroring task to the positionManagerReceiver, which
     * applies the appropriate mirroring transformation to a shape.
     *
     * The specific mirroring behavior depends on the value of mirrorDirection:
     * - UP: Mirrors the shape upward.
     * - DOWN: Mirrors the shape downward.
     * - LEFT: Mirrors the shape to the left.
     * - RIGHT: Mirrors the shape to the right.
     * - VERTICAL: Mirrors the shape vertically around its center.
     * - HORIZONTAL: Mirrors the shape horizontally around its center.
     *
     * This method is part of the implementation of the Command design pattern
     * and encapsulates the interaction logic for a specific mirroring action.
     */
    @Override
    public void executeCommand() {
        switch (mirrorDirection) {
            case UP: positionManagerReceiver.mirrorUp(); break;
            case DOWN: positionManagerReceiver.mirrorDown(); break;
            case LEFT: positionManagerReceiver.mirrorLeft(); break;
            case RIGHT: positionManagerReceiver.mirrorRight(); break;
            case VERTICAL: positionManagerReceiver.mirrorVertical(); break;
            case HORIZONTAL: positionManagerReceiver.mirrorHorizontal(); break;
        }

    }

    /**
     * Undoes the mirroring operation performed by this command.
     * This method reverses the direction of the mirroring based on the
     * initially executed mirror direction. It delegates the undoing logic
     * to the corresponding method of the {@code PositionManagerReceiver}.
     *
     * The undo logic is as follows:
     * - If the original mirroring direction was UP, the shape is mirrored DOWN.
     * - If the original mirroring direction was DOWN, the shape is mirrored UP.
     * - If the original mirroring direction was LEFT, the shape is mirrored RIGHT.
     * - If the original mirroring direction was RIGHT, the shape is mirrored LEFT.
     * - If the original mirroring direction was VERTICAL, the shape is mirrored vertically again.
     * - If the original mirroring direction was HORIZONTAL, the shape is mirrored horizontally again.
     */
    @Override
    public void undoCommand() {
        switch (mirrorDirection) {
            case UP: positionManagerReceiver.mirrorDown(); break;
            case DOWN: positionManagerReceiver.mirrorUp(); break;
            case LEFT: positionManagerReceiver.mirrorRight(); break;
            case RIGHT: positionManagerReceiver.mirrorLeft(); break;
            case VERTICAL: positionManagerReceiver.mirrorVertical(); break;
            case HORIZONTAL: positionManagerReceiver.mirrorHorizontal(); break;
        }
    }

    /**
     * Determines whether the mirroring operation is executable based on the position
     * simulated for the mirror direction.
     *
     * The method simulates the position change caused by the mirroring direction and
     * evaluates whether the resulting coordinates are within a specified tolerance.
     * If the simulated position is valid, the operation is executable.
     *
     * @return true if the mirror operation can be executed, either because the simulated
     *         position is empty or the coordinates are greater than or equal to the
     *         specified tolerance; false otherwise.
     */
    @Override
    public boolean isExecutable() {
        Optional<Point2D> simulated = positionManagerReceiver.simulateMirror(mirrorDirection);
        if (simulated.isEmpty()) return true;

        double x = simulated.get().getX();
        double y = simulated.get().getY();

        final double tolerance = -0.5;

        return x >= tolerance && y >= tolerance;
    }

}
