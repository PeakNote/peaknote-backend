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
     * Query all meeting transcripts by URL
     */
    // @GetMapping("/by-url")
    // public List<String> getTranscriptsByUrl(@RequestParam String url) {
    //     List<String> eventIds = transcriptService.getEventIdsByUrl(url);
    //     if (eventIds == null || eventIds.isEmpty()) {
    //         return List.of(); // Return empty list, frontend will get empty array []
    //     }
    //     return eventIds.stream()
    //         .map(transcriptService::getTranscriptByEventId)
    //         .collect(Collectors.toList());
    // }
    @GetMapping("/by-url")
    public Map<String, Object> getTranscriptsByUrl(@RequestParam String url) {
        try {
            if (url == null || url.trim().isEmpty()) {
                throw new IllegalArgumentException("URL parameter cannot be empty");
            }

            return transcriptService.getMeetingDataByUrl(url);
        } catch (Exception e) {
            System.err.println("❌ Failed to get meeting transcript: url=" + url + ", error=" + e.getMessage());
            e.printStackTrace();

            return Map.of(
                "meetingList", List.of(),
                "meetingDetails", Map.of("eventId", "", "transcript", ""),
                "error", "Failed to get meeting transcript: " + e.getMessage()
            );
        }
    }

    @GetMapping("/by-eventId")
    public Map<String, String> getTranscriptsByEventId(@RequestParam String eventId) {
        try {
            String transcript = transcriptService.getTranscriptByEventId(eventId);

            return Map.of(
                "eventId", eventId,
                "meetingTranscript", transcript
            );
        } catch (Exception e) {
            System.err.println("❌ Failed to get meeting transcript: eventId=" + eventId + ", error=" + e.getMessage());
            e.printStackTrace();
            return Map.of(
                "eventId", eventId,
                "meetingTranscript", "",
                "error", "Failed to get meeting transcript: " + e.getMessage()
            );
        }
    }


    /**
     * Update meeting transcript
     */
    @PostMapping("/update")
    public String updateTranscript(@RequestBody Map<String, String> request) {
        try {
            String eventId = request.get("eventId");
            String content = request.get("content");
            
            if (eventId == null || eventId.trim().isEmpty()) {
                throw new IllegalArgumentException("Event ID cannot be empty");
            }
            if (content == null) {
                throw new IllegalArgumentException("Content cannot be empty");
            }
            
            transcriptService.updateTranscript(eventId, content);
            return "success";
        } catch (Exception e) {
            System.err.println("❌ Failed to update meeting transcript: eventId=" + 
                request.get("eventId") + ", error=" + e.getMessage());
            e.printStackTrace();
            return "Update failed: " + e.getMessage();
        }
    }

}
