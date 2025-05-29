package org.example.model.commands;

import org.example.model.receivers.PositionManagerReceiver;

public class MirroringCommand implements CommandInterface{

    private PositionManagerReceiver positionManagerReceiver;
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    public MirroringCommand(PositionManagerReceiver positionManagerReceiver, boolean up, boolean down, boolean left, boolean right) {
        this.positionManagerReceiver = positionManagerReceiver;
        this.up = up;
        this.left = left;
        this.right = right;
        this.down = down;
    }

    @Override
    public void executeCommand() {
        if (up){
            positionManagerReceiver.mirrorUp();
            return;
        }
        if (down){
            positionManagerReceiver.mirrorDown();
            return;
        }
        if (left){
            positionManagerReceiver.mirrorLeft();
            return;
        }
        if (right){
            positionManagerReceiver.mirrorRight();
        }
    }

    @Override
    public void undoCommand() {
        if (up){
            positionManagerReceiver.mirrorDown();
            return;
        }
        if (down){
            positionManagerReceiver.mirrorUp();
            return;
        }
        if (left){
            positionManagerReceiver.mirrorRight();
            return;
        }
        if (right){
            positionManagerReceiver.mirrorLeft();
        }
    }
}
