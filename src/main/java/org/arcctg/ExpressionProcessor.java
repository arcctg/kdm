package org.arcctg;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class ExpressionProcessor {
    private static final List<String> COMPLETE_SET = List.of("0", "1", "2", "3",
            "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15");
    private final String NOT = "-";
    private final String NOTHING = "";

    private boolean form;
    private List<String> expressions;
    private Collector<CharSequence, ?, String> collector;

    public void process(String input) {
        List<String> inputNumbers = new ArrayList<>(List.of(input.split(" ")));
        form = inputNumbers.remove(0).equals("0");

        List<String> remainingNumbers = new ArrayList<>(COMPLETE_SET);
        remainingNumbers.removeAll(inputNumbers);

        processAndPrint(inputNumbers);
        form = !form;
        processAndPrint(remainingNumbers);
    }

    private void processAndPrint(List<String> numbers) {
        expressions = toListOfExpressions(numbers);
        printExpressionForm(String.join(", ", numbers));
        processGroups(expressions);
    }

    private List<String> toListOfExpressions(List<String> groups) {
        return groups.stream()
                .map(this::convertToExpression)
                .map(this::formatExpression)
                .toList();
    }

    private String convertToExpression(String value) {
        int number = Integer.parseInt(value);
        StringBuilder expression = new StringBuilder(8);

        for (int i = 3; i >= 0; i--) {
            boolean isSet = ((number >> i) & 1) == 1;
            expression.append(isSet == form ? NOT : NOTHING).append(4 - i);
        }
        return expression.toString();
    }

    private void printExpressionForm(String numbers) {
        collector = form
                ? Collectors.joining(")(", "(", ")")
                : Collectors.joining(" v ");

        System.out.printf("""
                
                P%sNF on a set of %s:
                %s
                """, form ? "C" : "D", numbers,
                expressions.stream().collect(collector));
    }

    private void processGroups(List<String> groups) {
        List<String> nextRound = new ArrayList<>();
        Set<String> usedGroups = new HashSet<>();
        AtomicInteger stepCounter = new AtomicInteger(1);

        System.out.println("\nImplicants:");
        for (int i = 0; i < groups.size(); i++) {
            String groupI = groups.get(i);

            for (int j = i + 1; j < groups.size(); j++) {
                String groupJ = groups.get(j);
                String merged = mergeGroups(groupI, groupJ);

                if (merged != null) {
                    usedGroups.add(groupI);
                    usedGroups.add(groupJ);

                    if (!nextRound.contains(merged)) {
                        System.out.printf("%d) %d-%d: %s\n", stepCounter.getAndIncrement(),
                                i + 1, j + 1, merged);
                        nextRound.add(merged);
                    } else {
                        System.out.printf("  —(%d-%d: %s)\n", i + 1, j + 1, merged);
                    }
                }
            }
        }

        groups.stream()
                .filter(group -> !usedGroups.contains(group))
                .forEach(unused -> {
                    System.out.printf("%d) %s\n", stepCounter.getAndIncrement(), unused);
                    nextRound.add(unused);
                });

        if (!usedGroups.isEmpty()) {
            processGroups(nextRound);
        } else {
            createShortTable(groups);
            findMin(groups);
        }
    }

    private String mergeGroups(String group1, String group2) {
        if (!group1.replace(NOT, NOTHING).equals(group2.replace(NOT, NOTHING))) {
            return null; // Groups differ in structure
        }

        List<String> components1 = parseComponents(group1);
        List<String> components2 = parseComponents(group2);

        StringBuilder merged = new StringBuilder();
        int differences = 0;

        for (int i = 0; i < components1.size(); i++) {
            if (components1.get(i).equals(components2.get(i))){
                merged.append(components1.get(i));
            } else {
                differences++;
            }

        }

        return differences == 1 ? formatExpression(merged.toString()) : null;
    }

    private void createShortTable(List<String> groups) {
        System.out.print("\n  ");
        for (int i = 1; i <= expressions.size(); i++) {
            System.out.printf("  %d ", i);
        }
        System.out.println();

        for (int i = 0; i < groups.size(); i++) {
            System.out.printf("%2d", i + 1);
            List<String> rowComponents = parseComponents(groups.get(i));

            for (String column : expressions) {
                boolean containsAll = parseComponents(column).containsAll(rowComponents);
                System.out.printf("| %s ", containsAll ? "X" : " ");
            }
            System.out.println();
        }
    }

    private void findMin(List<String> groups) {
        List<String> minResults = new ArrayList<>();
        Set<String> basicImplicants = new HashSet<>();
        List<List<String>> potentialCombinations = new ArrayList<>();
        Map<String, List<String>> parsedGroups = groups.stream()
                .collect(Collectors.toMap(group -> group, this::parseComponents));

        for (String expression : expressions) {
            Set<String> components = new HashSet<>(parseComponents(expression));

            potentialCombinations.add(
                    parsedGroups.entrySet().stream()
                            .filter(entry -> components.containsAll(entry.getValue()))
                            .map(Map.Entry::getKey)
                            .toList()
            );
        }

        potentialCombinations.removeIf(c -> c.size() == 1 && basicImplicants.add(c.get(0)));

        potentialCombinations.removeIf(c -> c.stream().anyMatch(basicImplicants::contains));

        String basics = basicImplicants.isEmpty() ? NOTHING :
                basicImplicants.stream()
                        .sorted(Comparator.comparingInt(s -> s.replace(NOT, NOTHING).length()))
                        .collect(collector);

        if (!potentialCombinations.isEmpty()) {
            generateCombinations(potentialCombinations).forEach(combination ->
                    minResults.add(basics.isEmpty() ? combination : basics + (form ? NOTHING : " v ") + combination)
            );
        } else {
            minResults.add(basics);
        }

        System.out.printf("\nVariants of min %sNF:\n", form ? "C" : "D");

        for (int i = 0; i < minResults.size(); i++) {
            System.out.printf("%d) %s\n", i + 1, minResults.get(i));
        }
    }

    private List<String> generateCombinations(List<List<String>> listOfLists) {
        List<String> result = new ArrayList<>();
        if (listOfLists == null || listOfLists.isEmpty()) {
            return result;
        }

        generateCombinationsHelper(listOfLists, 0, new ArrayList<>(), result);

        return result;
    }

    private void generateCombinationsHelper(List<List<String>> listOfLists,
                                            int index, List<String> current,
                                            List<String> result) {
        while (index < listOfLists.size() && listOfLists.get(index).stream()
                .anyMatch(current::contains)) {
            index++;
        }

        if (index == listOfLists.size()) {
            String currentSorted = current.stream()
                    .sorted(Comparator.comparingInt(String::length))
                    .collect(collector).trim();

            int length = currentSorted.replace(NOT, NOTHING).length();
            if (!result.isEmpty()) {
                int existingLength = result.get(0).replace(NOT, NOTHING).length();

                if (existingLength > length) {
                    result.clear();
                    result.add(currentSorted);
                } else if (existingLength == length) {
                    result.add(currentSorted);
                }
            } else {
                result.add(currentSorted);
            }
            return;
        }

        // Recursive combination generation
        for (String item : listOfLists.get(index)) {
            List<String> newCurrent = new ArrayList<>(current);
            newCurrent.add(item);
            generateCombinationsHelper(listOfLists, index + 1, newCurrent, result);
        }
    }

    private List<String> parseComponents(String s) {
        String group = s.replace("v", NOTHING);
        List<String> components = new ArrayList<>();

        for (int i = 0; i < group.length(); i++) {
            if (group.charAt(i) == NOT.charAt(0))
                components.add(group.substring(i, ++i + 1));
            else
                components.add(String.valueOf(group.charAt(i)));
        }

        return components;
    }

    private String formatExpression(String str) {
        StringBuilder builder = new StringBuilder();
        int length = str.length();

        for (int i = 0; i < length; i++) {
            builder.append(str.charAt(i)).append(str.charAt(i) == NOT.charAt(0) ?
                    str.charAt(++i) : NOTHING);
            if (form && i != length - 1) builder.append("v");
        }

        return builder.toString();
    }
}