package com.example.backend;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
public class HelloController {

    @GetMapping("/api/get_tag_by_count")
    public String[] getTagByCount() {//todo:finish
        String jsonFilePath = "D:\\java_proj\\backend\\src\\main\\java\\com\\example\\backend\\Data.json";
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Integer> tagCountMap = new HashMap<>();

        try (MappingIterator<StackOverflowQuestion> iterator = objectMapper.readerFor(StackOverflowQuestion.class)
                .readValues(new File(jsonFilePath))) {

            // 遍历每个StackOverflowQuestion对象并处理
            while (iterator.hasNext()) {
                StackOverflowQuestion question = iterator.next();
                List<String> tags = question.tags; // 假设getTags()返回List<String>

                // 统计每个tag的出现次数
                for (String tag : tags) {
                    tagCountMap.put(tag, tagCountMap.getOrDefault(tag, 0) + 1);

                }
            }

            // 找出数量最多的前十个tags
            List<Map.Entry<String, Integer>> topTags = tagCountMap.entrySet()
                    .stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .limit(10)
                    .collect(Collectors.toList());
            String[] results = new String[10];
            int index = 0;
            // 输出前十个tags及其数量
            for (Map.Entry<String, Integer> entry : topTags) {
                results[index] = "Tag: " + entry.getKey() + " Count: " + entry.getValue();
                index++;
            }
            return results;

        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
    }
    // ... existing code ...

    @GetMapping("/api/get_tag_by_engagement")
    public List<String> getTopNTopicsByEngagement() {//todo:finish
        String jsonFilePath = "D:\\java_proj\\backend\\src\\main\\java\\com\\example\\backend\\Data.json"; // JSON file path
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Integer> topicEngagementMap = new HashMap<>();
        List<Integer> reputations = new ArrayList<>();
        List<StackOverflowQuestion> questions = new ArrayList<>();

        try (MappingIterator<StackOverflowQuestion> iterator = objectMapper.readerFor(StackOverflowQuestion.class)
                .readValues(new File(jsonFilePath))) {

            while (iterator.hasNext()) {
                StackOverflowQuestion question = iterator.next();
                questions.add(question);
                reputations.add(question.owner.reputation);
                if (question.answers != null) {
                    for (Answer answer : question.answers) {
                        reputations.add(answer.owner.reputation);
                    }
                }
                if (question.comments != null) {
                    for (Comment comment : question.comments) {
                        reputations.add(comment.owner.reputation);
                    }
                }
            }

            double medianReputation = calculateMedian(reputations);

            for (StackOverflowQuestion question : questions) {
                int questionEngagement = calculateEngagement(question.owner.reputation, question, medianReputation);
                updateTopicEngagement(topicEngagementMap, question.tags, questionEngagement);

                if (question.answers != null) {
                    for (Answer answer : question.answers) {
                        int answerEngagement = calculateEngagement(answer.owner.reputation, answer, medianReputation);
                        updateTopicEngagement(topicEngagementMap, question.tags, answerEngagement);
                    }
                }

                if (question.comments != null) {
                    for (Comment comment : question.comments) {
                        int commentEngagement = calculateEngagement(comment.owner.reputation, comment, medianReputation);
                        updateTopicEngagement(topicEngagementMap, question.tags, commentEngagement);
                    }
                }
            }

            return getTopNTopics(topicEngagementMap, 10); // Assuming you want the top 10 topics

        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

//    @GetMapping("/api/get_top_mistake")
//    public List<String> getTopNMistake(){}
//
//    private Map<String, List<String>> findIssuesInQuestions(List<StackOverflowQuestion> questions) {
//        Map<String, List<String>> issuesMap = new HashMap<>();
//        Pattern pattern = Pattern.compile("\\b\\w*(error|exception)\\w*\\b", Pattern.CASE_INSENSITIVE);
//
//        for (StackOverflowQuestion question : questions) {
//            Matcher matcher = pattern.matcher(question.body);
//            List<String> issues = new ArrayList<>();
//
//            while (matcher.find()) {
//                issues.add(matcher.group());
//            }
//
//            if (!issues.isEmpty()) {
//                issuesMap.put(question.id, issues); // Assuming each question has a unique ID
//            }
//        }
//
//        return issuesMap;
//    }


    private int calculateEngagement(int reputation, Object activity, double medianReputation) {
        if (reputation <= medianReputation) {
            return 0;
        }

        if (activity instanceof StackOverflowQuestion) {
            return 2; // Each question gives its owner 2 activity points
        } else if (activity instanceof Answer answer) {
            return answer.is_accepted ? 5 : 2; // Accepted answers give 5 points, others give 2
        } else if (activity instanceof Comment) {
            return 1; // Each comment gives its owner 1 activity point
        }
        return 0;
    }

    private double calculateMedian(List<Integer> reputations) {
        Collections.sort(reputations);
        int size = reputations.size();
        if (size % 2 == 0) {
            return (reputations.get(size / 2 - 1) + reputations.get(size / 2)) / 2.0;
        } else {
            return reputations.get(size / 2);
        }
    }

// ... existing code ...

    private void updateTopicEngagement(Map<String, Integer> map, List<String> tags, int engagement) {
        for (String tag : tags) {
            map.put(tag, map.getOrDefault(tag, 0) + engagement);
        }
    }

    private List<String> getTopNTopics(Map<String, Integer> map, int N) {
        return map.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Sort by value in descending order
                .limit(N) // Limit to top N entries
                .map(entry -> "Engagement: " + entry.getKey() + " Count: " + entry.getValue()) // Format the output
                .collect(Collectors.toList()); // Collect to a list
    }
}