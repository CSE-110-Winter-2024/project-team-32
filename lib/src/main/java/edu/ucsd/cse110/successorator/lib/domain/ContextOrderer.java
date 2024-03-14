package edu.ucsd.cse110.successorator.lib.domain;

public class ContextOrderer {

    public ContextOrderer() {

    }


    public int compare(String context1, String context2) {
        Integer contextNum1 = 0;
        Integer contextNum2 = 0;

        switch(context1) {
            case "Home":
                contextNum1 = 0;
                break;
            case "Work":
                contextNum1 = 1;
                break;
            case "School":
                contextNum1 = 2;
                break;
            case "Errands":
                contextNum1 = 3;
                break;
        }

        switch(context2) {
            case "Home":
                contextNum2 = 0;
                break;
            case "Work":
                contextNum2 = 1;
                break;
            case "School":
                contextNum2 = 2;
                break;
            case "Errands":
                contextNum2 = 3;
                break;
        }

        return Integer.compare(contextNum1, contextNum2);
    }
}
