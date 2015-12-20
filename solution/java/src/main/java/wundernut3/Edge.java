package wundernut3;

public enum Edge {
    FoxHead(Animal.Fox, Part.Head),
    FoxTorso(Animal.Fox, Part.Torso),
    DeerHead(Animal.Deer, Part.Head),
    DeerTorso(Animal.Deer, Part.Torso),
    RaccoonHead(Animal.Raccoon, Part.Head),
    RaccoonTorso(Animal.Raccoon, Part.Torso);

    public final Animal animal;
    public final Part part;

    private Edge(Animal animal, Part part) {
        this.animal = animal;
        this.part = part;
    }

    public boolean matches(Edge other) {
        if (this.animal == other.animal && this.part != other.part) {
            return true;
        }

        return false;
    }

    private enum Animal { Fox, Deer, Raccoon }

    private enum Part { Head, Torso }
}
