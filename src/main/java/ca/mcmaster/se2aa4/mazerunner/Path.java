package ca.mcmaster.se2aa4.mazerunner;

import java.util.ArrayList;
import java.util.List;
/**
 * Name: Yug Vashisth, MacID: vashisty, Student number: 400501750
 * The Path class is responsible for getting the canonical and factorized form of the path.
 * it calculates the factorized path, and displays the canoincal straight up once converting tostring
 */
public class Path {
    private final List<Character> steps;

    /**
     * Constructs an empty Path object.
     */
    public Path() {
        this.steps = new ArrayList<>();
    }

    /**
     * adds step
     */
    public void addStep(char step) {
        steps.add(step);
    }

    /**
     * Returns canonical form of the path.
     */
    public String getCanonicalForm() {
        StringBuilder canonicalForm = new StringBuilder();
        for (char step : steps) {
            canonicalForm.append(step);
        }
        return canonicalForm.toString();
    }

    /**
     * Returns factorized form of the path.
     */

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

        return factorized.toString(); //tostring method to return it in proper output format
    }
}
