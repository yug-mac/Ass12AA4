package ca.mcmaster.se2aa4.mazerunner;

public class Path {
    private final StringBuilder steps = new StringBuilder();

    public void addStep(char step) {
        steps.append(step);
    }

    public String getCanonicalForm() {
        return steps.toString();
    }

    public String getFactorizedForm() {
        StringBuilder factorized = new StringBuilder();
        int count = 1;

        for (int i = 1; i < steps.length(); i++) {
            if (steps.charAt(i) == steps.charAt(i - 1)) {
                count++;
            } else {
                factorized.append(count > 1 ? count : "").append(steps.charAt(i - 1));
                count = 1;
            }
        }

        factorized.append(count > 1 ? count : "").append(steps.charAt(steps.length() - 1));
        return factorized.toString();
    }
}
