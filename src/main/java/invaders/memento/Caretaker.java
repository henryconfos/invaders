package invaders.memento;

public class Caretaker {
    private Memento memento;

    public void saveStateToMemento(Memento memento) {
        this.memento = memento;
    }

    public Memento getStateFromMemento() {
        return memento;
    }
}
