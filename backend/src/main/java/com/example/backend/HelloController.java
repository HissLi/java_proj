package com.example.backend;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class HelloController {

    @GetMapping("/api/get_tag_by_count")
    public String[] getTagByCount() {
        String jsonFilePath = "C:\\Users\\nameless27\\Desktop\\Data.json"; // JSON文件路径
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Integer> tagCountMap = new HashMap<>();

        try (MappingIterator<StackOverflowQuestion> iterator = objectMapper.readerFor(StackOverflowQuestion.class)
                .readValues(new File(jsonFilePath))) {

            // 遍历每个StackOverflowQuestion对象并处理
            while (iterator.hasNext()) {
                StackOverflowQuestion question = iterator.next();
                List<String> tags = question.getTags(); // 假设getTags()返回List<String>

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
                results[index] = "Tag: " + entry.getKey() + ", Count: " + entry.getValue();
                index++;
            }
            return results;

        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
    }
}