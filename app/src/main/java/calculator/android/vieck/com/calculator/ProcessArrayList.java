package calculator.android.vieck.com.calculator;

import android.util.Log;

import java.util.ArrayList;

public class ProcessArrayList {

    private static final String DEBUG_TAG = "DEBUG";

    public ArrayList<String> processArray(ArrayList<String> evaluatedArrayList) {

        while (evaluatedArrayList.size() > 1) {
            try {
                String firstNumberString = evaluatedArrayList.get(0);
                if (firstNumberString.equals("(")) {
                    int inParenthIndex = 0;
                    int outParenthIndex = 0;
                    for (int i = inParenthIndex; i < evaluatedArrayList.size(); i++) {
                        if (evaluatedArrayList.get(i).equals(")") && i != evaluatedArrayList.size() - 1 && !evaluatedArrayList.get(i + 1).equals(")")) {
                            outParenthIndex = i;
                            break;
                        } else if (evaluatedArrayList.get(i).equals(")") && i == evaluatedArrayList.size() - 1) {
                            outParenthIndex = i;
                        }
                    }
                    Log.d(DEBUG_TAG, "Before ArrayList: " + evaluatedArrayList.toString());
                    ArrayList<String> tempList = new ArrayList<>();
                    for (int i = 1; i < outParenthIndex; i++) {
                        tempList.add(evaluatedArrayList.get(i));
                    }
                    tempList = processArray(tempList);
                    Log.d(DEBUG_TAG, "Temporary ArrayList: " + tempList.toString());
                    for (int i = 1; i <= outParenthIndex; i++) {
                        evaluatedArrayList.remove(1);
                    }
                    Log.d(DEBUG_TAG, "Evaluated ArrayList: " + evaluatedArrayList.toString());
                    evaluatedArrayList.set(0, tempList.get(0));
                    Log.d(DEBUG_TAG, "PostSet ArrayList: " + evaluatedArrayList.toString());

                } else if (evaluatedArrayList.get(1).equals("(")) {
                    String parenthesis = evaluatedArrayList.get(1);
                    int inParenthIndex = evaluatedArrayList.indexOf(parenthesis);
                    int outParenthIndex = -1;
                    for (int i = evaluatedArrayList.size() - 1; i > 0; i--) {
                        if (evaluatedArrayList.get(i).equals(")")) {
                            outParenthIndex = evaluatedArrayList.indexOf(evaluatedArrayList.get(i));
                        }
                    }
                    if (outParenthIndex != -1) {
                        ArrayList<String> tempList = new ArrayList<>();
                        for (int i = inParenthIndex; i <= outParenthIndex; i++) {
                            tempList.add(evaluatedArrayList.get(i));
                        }
                        tempList = processArray(tempList);
                        parenthesis = tempList.get(0);
                        for (int i = inParenthIndex; i < outParenthIndex; i++) {
                            evaluatedArrayList.remove(inParenthIndex);
                        }
                        evaluatedArrayList.set(inParenthIndex, parenthesis);
                    }
                    float firstNumber = Float.parseFloat(firstNumberString);
                    float secondNumber = Float.parseFloat(parenthesis);
                    Log.d(DEBUG_TAG, "Parenthesis: First and Second Number: " + firstNumberString + " " + secondNumber);
                    float answer = firstNumber * secondNumber;
                    Log.d(DEBUG_TAG, "Answer: " + answer);
                    String pushString = String.valueOf(answer);
                    evaluatedArrayList.remove(1);
                    evaluatedArrayList.set(0, pushString);

                } else if (evaluatedArrayList.get(1).equals("×")) {
                    String secondNumberString = evaluatedArrayList.get(2);
                    if (secondNumberString.equals("(")) {
                        int inParenthIndex = evaluatedArrayList.indexOf(secondNumberString);
                        int outParenthIndex = -1;
                        for (int i = evaluatedArrayList.size() - 1; i > 0; i--) {
                            if (evaluatedArrayList.get(i).equals(")")) {
                                outParenthIndex = evaluatedArrayList.indexOf(evaluatedArrayList.get(i));
                            }
                        }
                        if (outParenthIndex != -1) {
                            ArrayList<String> tempList = new ArrayList<>();
                            for (int i = inParenthIndex; i <= outParenthIndex; i++) {
                                tempList.add(evaluatedArrayList.get(i));
                            }
                            tempList = processArray(tempList);
                            secondNumberString = tempList.get(0);
                            for (int i = inParenthIndex; i < outParenthIndex; i++) {
                                evaluatedArrayList.remove(inParenthIndex);
                            }
                            evaluatedArrayList.set(inParenthIndex, secondNumberString);
                        }
                    }
                    float firstNumber = Float.parseFloat(firstNumberString);
                    float secondNumber = Float.parseFloat(secondNumberString);
                    Log.d(DEBUG_TAG, "Multiplication: First and Second Number: " + firstNumberString + " " + secondNumberString);
                    float answer = firstNumber * secondNumber;
                    String pushString = String.valueOf(answer);
                    evaluatedArrayList.remove(1);
                    evaluatedArrayList.remove(1);
                    evaluatedArrayList.set(0, pushString);

                } else if (evaluatedArrayList.get(1).equals("÷")) {
                    String secondNumberString = evaluatedArrayList.get(2);
                    Log.d(DEBUG_TAG, "Before Evaluated Array: " + evaluatedArrayList.toString());
                    if (secondNumberString.equals("(")) {
                        int inParenthIndex = evaluatedArrayList.indexOf(secondNumberString);
                        int outParenthIndex = -1;
                        for (int i = evaluatedArrayList.size() - 1; i > 0; i--) {
                            if (evaluatedArrayList.get(i).equals(")")) {
                                outParenthIndex = evaluatedArrayList.indexOf(evaluatedArrayList.get(i));
                                Log.d(DEBUG_TAG, "Specific Index +" + outParenthIndex + " " + i);
                            }
                        }
                        if (outParenthIndex != -1) {
                            ArrayList<String> tempList = new ArrayList<>();
                            for (int i = inParenthIndex; i <= outParenthIndex; i++) {
                                tempList.add(evaluatedArrayList.get(i));
                            }
                            tempList = processArray(tempList);
                            secondNumberString = tempList.get(0);
                            for (int i = inParenthIndex; i < outParenthIndex; i++) {
                                evaluatedArrayList.remove(inParenthIndex);
                            }
                            evaluatedArrayList.set(inParenthIndex, secondNumberString);
                        }
                    }
                    float firstNumber = Float.parseFloat(firstNumberString);
                    float secondNumber = Float.parseFloat(secondNumberString);
                    if (secondNumber == 0.0) {
                        throw new Exception();
                    }
                    Log.d(DEBUG_TAG, "Division: First and Second Number: " + firstNumberString + " " + secondNumberString);
                    float answer = firstNumber / secondNumber;
                    String pushString = String.valueOf(answer);
                    Log.d(DEBUG_TAG, "Evaluated: " + evaluatedArrayList.size());
                    evaluatedArrayList.remove(1);
                    evaluatedArrayList.remove(1);
                    evaluatedArrayList.set(0, pushString);

                } else if (evaluatedArrayList.get(1).contains("+")) {

                    String secondNumberString = evaluatedArrayList.get(2);
                    if (secondNumberString.equals("(")) {
                        int inParenthIndex = evaluatedArrayList.indexOf(secondNumberString);
                        int outParenthIndex = -1;
                        for (int i = evaluatedArrayList.size() - 1; i > 0; i--) {
                            if (evaluatedArrayList.get(i).equals(")")) {
                                outParenthIndex = evaluatedArrayList.indexOf(evaluatedArrayList.get(i));
                            }
                        }
                        if (outParenthIndex != -1) {
                            ArrayList<String> tempList = new ArrayList<>();
                            for (int i = inParenthIndex; i <= outParenthIndex; i++) {
                                tempList.add(evaluatedArrayList.get(i));
                            }
                            tempList = processArray(tempList);
                            secondNumberString = tempList.get(0);
                            for (int i = inParenthIndex; i < outParenthIndex; i++) {
                                evaluatedArrayList.remove(inParenthIndex);
                            }
                            evaluatedArrayList.set(inParenthIndex, secondNumberString);
                        }
                    }
                    Log.d(DEBUG_TAG, "Addition: First and Second Number: " + firstNumberString + " " + secondNumberString);
                    float firstNumber = Float.parseFloat(firstNumberString);
                    float secondNumber = Float.parseFloat(secondNumberString);
                    float answer = firstNumber + secondNumber;
                    String pushString = String.valueOf(answer);
                    evaluatedArrayList.remove(1);
                    evaluatedArrayList.remove(1);
                    evaluatedArrayList.set(0, pushString);

                } else if (evaluatedArrayList.get(1).equals("-")) {
                    String secondNumberString = evaluatedArrayList.get(2);
                    if (secondNumberString.equals("(")) {
                        int inParenthIndex = evaluatedArrayList.indexOf(secondNumberString);
                        int outParenthIndex = -1;
                        for (int i = evaluatedArrayList.size() - 1; i > 0; i--) {
                            if (evaluatedArrayList.get(i).equals(")")) {
                                outParenthIndex = evaluatedArrayList.indexOf(evaluatedArrayList.get(i));
                            }
                        }
                        if (outParenthIndex != -1) {
                            ArrayList<String> tempList = new ArrayList<>();
                            for (int i = inParenthIndex; i <= outParenthIndex; i++) {
                                tempList.add(evaluatedArrayList.get(i));
                            }
                            tempList = processArray(tempList);
                            secondNumberString = tempList.get(0);
                            for (int i = inParenthIndex; i < outParenthIndex; i++) {
                                evaluatedArrayList.remove(inParenthIndex);
                            }
                            evaluatedArrayList.set(inParenthIndex, secondNumberString);
                        }
                    }
                    Log.d(DEBUG_TAG, "Subtraction: First and Second Number: " + firstNumberString + " " + secondNumberString);
                    float firstNumber = Float.parseFloat(firstNumberString);
                    float secondNumber = Float.parseFloat(secondNumberString);
                    float answer = firstNumber - secondNumber;
                    String pushString = String.valueOf(answer);
                    evaluatedArrayList.remove(1);
                    evaluatedArrayList.remove(1);
                    evaluatedArrayList.set(0, pushString);

                } else {
                    break;
                }
            } catch(Exception nfe){
                evaluatedArrayList.clear();
                evaluatedArrayList.add("Error");
                return evaluatedArrayList;
            }
        }
        return evaluatedArrayList;
    }
}
