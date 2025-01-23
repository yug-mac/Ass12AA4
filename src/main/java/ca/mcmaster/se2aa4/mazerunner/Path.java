package ca.mcmaster.se2aa4.mazerunner;

public class Path {
    private final StringBuilder steps = new StringBuilder();

    public void addStep(char step) {
        steps.append(step);
    }

    public String getCanonicalForm() {
        return steps.toString();
    }

}
