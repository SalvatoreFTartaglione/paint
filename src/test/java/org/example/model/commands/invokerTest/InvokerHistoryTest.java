package org.example.model.commands.invokerTest;

import org.example.model.commandInterface.CommandInterface;
import org.example.model.invoker.Invoker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InvokerHistoryTest {

    private static class CountingCommand implements CommandInterface {
        int executed;
        int undone;

        @Override
        public void executeCommand() {
            executed++;
        }

        @Override
        public void undoCommand() {
            undone++;
        }
    }

    @Test
    public void undoRevertsCommandsInReverseOrder() {
        Invoker invoker = new Invoker();
        CountingCommand first = new CountingCommand();
        CountingCommand second = new CountingCommand();
        invoker.execute(first);
        invoker.execute(second);

        invoker.undo();

        assertEquals(1, second.undone);
        assertEquals(0, first.undone);
    }

    @Test
    public void clearHistoryForgetsEveryCommand() {
        Invoker invoker = new Invoker();
        CountingCommand command = new CountingCommand();
        invoker.execute(command);

        invoker.clearHistory();
        invoker.undo();

        assertEquals(0, command.undone);
    }

    @Test
    public void undoOnEmptyHistoryDoesNothing() {
        assertDoesNotThrow(() -> new Invoker().undo());
    }
}
