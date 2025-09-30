package com.peaknote.demo.service;

import java.time.Instant;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;


@Service
public class MeetingSummaryService {

    private final ChatClient chatClient;

    public MeetingSummaryService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String generateSummary(Instant startTime,String transcriptContent) {
      String prompt = """
        You are an expert AI assistant specializing in intelligent meeting summarization. Your task is to analyze a provided meeting transcript to understand its core purpose, tone, and content flow, then design and populate a tailored summary. **Return your result strictly as a valid JSON document** that matches the TipTap/ProseMirror-like structure shown in the example that follows (root `"type": "doc"` with a `"content"` array of nodes).

        **Process**

        1. **Analyze the transcript**

        * Determine the meeting type (e.g., formal business review, technical debrief, brainstorming, casual sync).
        * Identify main speakers/roles, central topics, goals, decisions, and tone.
        * Extract concrete data: date/time (convert and display in **AEST / Australia/Sydney**), location/platform (online), attendees, agenda, key points, risks, decisions, next steps.

        2. **Design a logical structure**

        * Create section headings that reflect the real flow of the conversation—**no generic template.**
        * Examples: for a project update use sections like “Progress on Key Initiatives,” “Risks/Blockers,” “Release Readiness”; for a brainstorm use “Idea Themes,” “Shortlisted Concepts,” “Evaluation Criteria,” etc.
        * Include sections only if they are supported by the transcript. Do **not** invent content.

        3. **Generate the summary (as JSON)**

        * Output must follow the node schema used in the example:

          * Root: `{ "type": "doc", "content": [ ... ] }`
          * Use nodes: `"heading"`, `"paragraph"`, `"bulletList"`/`"orderedList"` with `"listItem"`, `"taskList"` with `"taskItem"`.
          * For headings, set `"attrs": { "textAlign": "left", "level": N }` where `N` is 1 for title, 2 for top-level sections, 3 for subsections if needed.
          * For paragraphs, set `"attrs": { "textAlign": "left" }`.
          * Use `"marks": [{ "type": "bold" }]` to emphasize labels like **Date:**, **Location:**, **Attendees:** inside paragraph nodes when appropriate.
          * Represent attendees as a `"bulletList"` of `"listItem"` names with roles.
          * Represent agenda as an `"orderedList"` if present.
          * Represent action items as a `"taskList"` of `"taskItem"` entries; include assignee and due date in the text; set `"attrs": { "checked": false }` unless the transcript confirms completion.
          * Represent decisions as a `"bulletList"`.
          * Represent “Next Meeting” as a `"paragraph"` or its own section, with date/time in **AEST** and platform.

        **Formatting & Validation Rules**

        * **Return JSON only** (no prose, no markdown, no HTML, no comments).
        * The JSON must be syntactically valid, UTF-8, with no trailing commas.
        * Do not include styling or CSS—only content and the specified node/attrs structure.
        * Keep text concise and faithful to the transcript; avoid unverifiable speculation.
        * If specific data (e.g., next meeting time) is not discussed, omit that section rather than guessing.
        * All times displayed should be normalized to **AEST (Australia/Sydney)** and labeled in the text if relevant.
        * The meeting is conducted online; reflect the platform if known (e.g., “Zoom,” “Google Meet”), otherwise “Online (AEST).”

        **Output Shape**

        * Mirror the structure and node types of the provided example exactly (e.g., `"heading"` with `level: 1` for the document title, then metadata as a `"paragraph"` with bold labels, followed by tailored sections).
        * Prefer the following high-level order when applicable (customize as needed): Title → Metadata (Date/Location) → Attendees → Agenda → Thematic Sections (tailored) → Action Items → Decisions → Next Meeting.

        **Example to follow**
        Use the same node types, attributes, and general structure as in the sample JSON provided by the user (do not copy its content; generate new content from the transcript).

      """ + startTime.toString();




        // Spring AI 1.0.0 new syntax, chained
        return chatClient
                .prompt(prompt)
                .user(transcriptContent)
                .call()
                .content();
    }
}
