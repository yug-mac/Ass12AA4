package ca.mcmaster.se2aa4.mazerunner;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private final List<Character> steps;

    public Path() {
        this.steps = new ArrayList<>();
    }

    public void addStep(char step) {
        steps.add(step);
    }

    public String getCanonicalForm() {
        StringBuilder canonicalForm = new StringBuilder();
        for (char step : steps) {
            canonicalForm.append(step);
        }
        return canonicalForm.toString();
    }

    public String getFactorized() {
        if (steps.isEmpty()) {
            return "";
        }

        StringBuilder factorized = new StringBuilder();
        char currentStep = steps.get(0);
        int count = 1;

        for (int i = 1; i < steps.size(); i++) {
            if (steps.get(i) == currentStep) {
                count++;
            } else {
                factorized.append(count).append(currentStep);
                currentStep = steps.get(i);
                count = 1;
            }
        }
        factorized.append(count).append(currentStep);

        return factorized.toString();
    }
}
