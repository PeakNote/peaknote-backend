package com.peaknote.demo.controller;

import com.peaknote.demo.service.TranscriptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/transcript")
public class TranscriptController {

    private final TranscriptService transcriptService;

    /**
     * 根据 URL 查询所有会议纪要
     */
    // @GetMapping("/by-url")
    // public List<String> getTranscriptsByUrl(@RequestParam String url) {
    //     List<String> eventIds = transcriptService.getEventIdsByUrl(url);
    //     if (eventIds == null || eventIds.isEmpty()) {
    //         return List.of(); // 返回空 list，前端就会得到空数组 []
    //     }
    //     return eventIds.stream()
    //         .map(transcriptService::getTranscriptByEventId)
    //         .collect(Collectors.toList());
    // }
    @GetMapping("/by-url")
    public Map<String, String> getTranscriptsByUrl(@RequestParam String url) {
        //String decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8);

        List<String> eventIds = transcriptService.getEventIdsByUrl(url);
        if (eventIds == null || eventIds.isEmpty()) {
            return Map.of("transcript", "");
        }
        String transcript = transcriptService.getTranscriptByEventId(eventIds.get(0));
        return Map.of("eventId",eventIds.get(0),"transcript", transcript);
    }

    /**
     * 更新会议纪要
     */
    @PostMapping("/update")
    public String updateTranscript(@RequestParam String eventId, @RequestParam String content) {
        transcriptService.updateTranscript(eventId, content);
        return "✅ success";
    }

    
}
