package org.example.model.commands;

import org.example.model.receivers.PositionManagerReceiver;

public class MirroringCommand implements CommandInterface{

    private PositionManagerReceiver positionManagerReceiver;
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private boolean horizontal;
    private boolean vertical;

    public MirroringCommand(PositionManagerReceiver positionManagerReceiver, boolean up, boolean down, boolean left, boolean right, boolean horizontal, boolean vertical) {
        this.positionManagerReceiver = positionManagerReceiver;
        this.up = up;
        this.left = left;
        this.right = right;
        this.down = down;
        this.horizontal = horizontal;
        this.vertical = vertical;
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
            return;
        }
        if (horizontal){
            positionManagerReceiver.mirrorHorizontal();
            return;
        }
        if (vertical){
            positionManagerReceiver.mirrorVertical();
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
            return;
        } if (horizontal){
            positionManagerReceiver.mirrorHorizontal();
            return;
        }
        if (vertical){
            positionManagerReceiver.mirrorVertical();
        }
    }
}
