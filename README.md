# PeakNote - Microsoft Teams Meeting Intelligence Platform

## Overview

PeakNote is a comprehensive meeting intelligence platform that integrates with Microsoft Teams to automatically capture, transcribe, and analyze meeting content. The platform provides intelligent meeting summaries, attendee tracking, and seamless integration with Microsoft Graph API for real-time meeting event processing.

## Features

### Core Capabilities
- **Real-time Meeting Event Processing**: Automatic capture of Microsoft Teams meeting events via webhooks
- **Intelligent Transcript Processing**: AI-powered meeting transcript analysis and summarization using Llama 3.3
- **Attendee Management**: Comprehensive tracking of meeting participants
- **Meeting URL Sharing**: Secure access control for meeting sharing
- **Subscription Management**: Automated Microsoft Graph API subscription lifecycle management
- **Email Integration**: Automated email notifications and PDF report generation
- **Scheduled Synchronization**: Automated user and meeting data synchronization

### AI-Powered Features
- **Meeting Summaries**: Generate structured meeting minutes using Llama 3.3-70B-Instruct
- **Dynamic Template Generation**: AI creates custom summary templates based on meeting content
- **HTML Report Generation**: Professional meeting reports in HTML format
- **Content Categorization**: Automatic identification of action items and discussion points

### Integration Features
- **Microsoft Graph API**: Full integration with Teams calendar and meeting data
- **Webhook Processing**: Real-time event handling for meeting lifecycle
- **Message Queue System**: Asynchronous processing using RabbitMQ
- **Caching Layer**: Redis-based caching with Redisson for improved performance
- **PDF Generation**: OpenHTMLToPDF integration for report generation

## Architecture

### System Components
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Microsoft     │    │   PeakNote      │    │   External      │
│   Teams/Graph   │◄──►│   Backend       │◄──►│   Services      │
│   Webhooks      │    │   (Spring Boot) │    │   (Llama AI)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              ▼
                    ┌─────────────────────────┐
                    │     Data Layer          │
                    │  ┌─────┐ ┌─────┐ ┌─────┐│
                    │  │MySQL│ │Redis│ │Rabbit││
                    │  │     │ │     │ │  MQ ││
                    │  └─────┘ └─────┘ └─────┘│
                    └─────────────────────────┘
```

### Docker Services
The application includes Docker Compose configuration for local development:
- **MySQL 8.0**: Database with health checks
- **Redis 7**: Caching layer with health checks  
- **RabbitMQ 3.8**: Message queue with management UI (port 15672)
![SystemDiagram](https://github.com/PeakNote/peaknote-backend/blob/develop/Blank%20diagram.jpeg)


### Technology Stack
- **Framework**: Spring Boot 3.4.4
- **Language**: Java 17
- **Database**: MySQL 8.0 with JPA/Hibernate
- **Cache**: Redis 7 with Redisson 3.28.0
- **Message Queue**: RabbitMQ 3.8+ with Management UI
- **AI Integration**: Llama 3.3-70B-Instruct via Spring AI 1.0.0-M6
- **Authentication**: Azure AD with Microsoft Graph 5.70.0
- **PDF Generation**: OpenHTMLToPDF 1.0.10
- **Email**: Spring Boot Mail with Gmail SMTP
- **Build Tool**: Maven
- **Additional**: Lombok 1.18.32, Azure Identity 1.10.4

## Quick Start

### Prerequisites
- Java 17 or higher
- Docker and Docker Compose (for local development)
- Microsoft Azure App Registration
- Llama API Key (or compatible AI service)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd PeakNote
   ```

2. **Start Docker services**
   ```bash
   docker-compose up -d
   ```
   This will start MySQL, Redis, and RabbitMQ with health checks.

3. **Configure environment variables**
   Update `src/main/resources/application.yml` with your configuration:
   ```yaml
   spring:
     application:
       name: demo
     
     datasource:
       url: jdbc:mysql://localhost:3306/peaknote?useSSL=false&serverTimezone=UTC&characterEncoding=utf-8
       username: root
       password: 123456
       driver-class-name: com.mysql.cj.jdbc.Driver
     
     ai:
       openai:
         base-url: ${SPRING_AI_OPENAI_BASE_URL:https://api.llama.com/compat/}
         api-key: ${SPRING_AI_OPENAI_API_KEY:your_llama_api_key}
         chat:
           options:
             model: ${SPRING_AI_OPENAI_CHAT_MODEL:Llama-3.3-70B-Instruct}
     
     rabbitmq:
       host: localhost
       port: 5672
       username: guest
       password: guest
     
     data:
       redis:
         host: localhost
         port: 6379
         password: # optional
         timeout: 5000
     
     cache:
       type: redis
       redis:
         time-to-live: 600000
         cache-null-values: true
     
     jpa:
       hibernate:
         ddl-auto: update
       show-sql: true
       properties:
         hibernate:
           format_sql: true
     
     mail:
       host: smtp.gmail.com
       port: 587
       username: your_email@gmail.com
       password: your_app_password
       properties:
         mail:
           smtp:
             auth: true
             starttls:
               enable: true
             debug: true
           debug: true

   azure:
     tenant-id: your_azure_tenant_id
     client-id: your_azure_client_id
     client-secret: your_azure_client_secret
     graph:
       scope: https://graph.microsoft.com/.default

   webhook:
     tenant-id: your_webhook_tenant_id
     client-id: your_webhook_client_id
     client-secret: your_webhook_client_secret

   notification-url: https://your-domain.com/webhook/notification
   ```

4. **Build the application**
   ```bash
   mvn clean install
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

6. **Access services**
   - Application: http://localhost:8080
   - RabbitMQ Management: http://localhost:15672 (guest/guest)
   - Database: localhost:3306 (root/123456)
   - Redis: localhost:6379

### Application Startup Process
The application automatically performs the following on startup:
1. **User Synchronization**: Syncs Microsoft Teams users from Graph API
2. **Subscription Cleanup**: Removes previous Graph API subscriptions
3. **Subscription Registration**: Creates new webhook subscriptions for all users
4. **Health Checks**: Verifies all service connections

# PeakNote API Documentation

## Overview
This document provides comprehensive API documentation for the PeakNote application, covering attendee management and transcript services.

## Base URL
```
http://localhost:8080
```

## Authentication
Currently, the API does not require authentication for the documented endpoints.

---

## Attendee Management API

### Get Meeting Attendees

Retrieves all attendees for a specific meeting event.

**Endpoint:** `GET /attendees`

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `eventId` | String | Yes | The unique identifier of the meeting event |

**Response Format:**
```json
{
  "eventId": "string",
  "attendees": [
    {
      "email": "user@example.com",
      "displayName": "John Doe"
    }
  ]
}
```

**Response Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `eventId` | String | The meeting event identifier |
| `attendees` | Array | List of meeting attendees |
| `attendees[].email` | String | Attendee's email address |
| `attendees[].displayName` | String | Attendee's display name |

**Example Request:**
```bash
curl -X GET "http://localhost:8080/attendees?eventId=meeting-123"
```

**Example Response:**
```json
{
  "eventId": "meeting-123",
  "attendees": [
    {
      "email": "john.doe@company.com",
      "displayName": "John Doe"
    },
    {
      "email": "jane.smith@company.com",
      "displayName": "Jane Smith"
    }
  ]
}
```

**HTTP Status Codes:**
- `200 OK` - Successfully retrieved attendees
- `400 Bad Request` - Missing or invalid eventId parameter
- `500 Internal Server Error` - Server error

---

## Transcript Management API

### Get Transcript by URL

Retrieves meeting transcript information based on the meeting URL.

**Endpoint:** `GET /transcript/by-url`

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `url` | String | Yes | The meeting URL to search for transcripts |

**Response Format:**
```json
{
  "eventId": "string",
  "transcript": "string"
}
```

**Response Fields:**
| Field | Type | Description |
|-------|------|-------------|
| `eventId` | String | The meeting event identifier (if found) |
| `transcript` | String | The meeting transcript content |

**Example Request:**
```bash
curl -X GET "http://localhost:8080/transcript/by-url?url=https://teams.microsoft.com/l/meetup-join/..."
```

**Example Response:**
```json
{
  "eventId": "meeting-456",
  "transcript": "Meeting started at 10:00 AM. John: Hello everyone... Jane: Good morning..."
}
```

**Error Response (No transcript found):**
```json
{
  "transcript": "",
  "error": "Failed to get meeting transcript: URL does not exist"
}
```

**HTTP Status Codes:**
- `200 OK` - Successfully retrieved transcript or no transcript found
- `400 Bad Request` - Missing or invalid URL parameter
- `500 Internal Server Error` - Server error

### Update Transcript

Updates the transcript content for a specific meeting event using JSON format.

**Endpoint:** `POST /transcript/update`

**Request Body (JSON):**
```json
{
  "eventId": "string",
  "content": "string"
}
```

**Request Fields:**
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `eventId` | String | Yes | The unique identifier of the meeting event |
| `content` | String | Yes | The new transcript content |

**Response Format:**
```
success
```

**Example Request:**
```bash
curl -X POST "http://localhost:8080/transcript/update" \
  -H "Content-Type: application/json" \
  -d '{"eventId": "meeting-456", "content": "Updated meeting transcript content..."}'
```

**Example Response:**
```
success
```

**Error Response:**
```
Update failed: Event ID cannot be empty
```

**HTTP Status Codes:**
- `200 OK` - Successfully updated transcript
- `400 Bad Request` - Missing or invalid parameters
- `500 Internal Server Error` - Server error

---

## Email & Reports API

### Send Email
- `POST /mail/send` - Send email notifications

### Generate PDF
- `POST /mail/generate-pdf` - Generate PDF reports from meeting data

---

## Webhook Endpoints

### Microsoft Graph Webhooks
- `GET /webhook/notification` - Microsoft Graph webhook validation
- `POST /webhook/notification` - Handle meeting event notifications
- `POST /webhook/teams-transcript` - Handle transcript notifications
- `POST /webhook/teams-lifecycle` - Handle Graph subscription lifecycle events

---

## Debug & Monitoring

### System Status
- `GET /debug/status` - Application health and status
- `GET /debug/subscriptions` - View active Graph subscriptions
- `GET /debug/queues` - Monitor message queue status

---

## CORS Support

The Transcript API supports Cross-Origin Resource Sharing (CORS) with the following configuration:
- **Allowed Origins:** `*` (All origins)
- **Methods:** GET, POST
- **Headers:** All headers

---

## Error Handling

### Common Error Responses

**400 Bad Request:**
```json
{
  "transcript": "",
  "error": "Failed to get meeting transcript: URL parameter cannot be empty"
}
```

**500 Internal Server Error:**
```
Update failed: An unexpected error occurred
```

---

## Data Models

### Meeting Attendee
```json
{
  "email": "string",
  "displayName": "string"
}
```

### Transcript Response
```json
{
  "eventId": "string",
  "transcript": "string"
}
```

### Transcript Update Request
```json
{
  "eventId": "string",
  "content": "string"
}
```

---

## Notes

1. **Caching:** The transcript service implements Redis caching for improved performance:
   - URL to event ID mapping is cached
   - Transcript content is cached by event ID
   - Cache is automatically invalidated when transcripts are updated

2. **URL Encoding:** Meeting URLs should be properly URL-encoded when passed as query parameters.

3. **Rate Limiting:** Currently, no rate limiting is implemented on these endpoints.

4. **Logging:** All operations are logged for debugging and monitoring purposes.

5. **JSON Format:** The transcript update endpoint now uses JSON format instead of form parameters.

---

## Version Information

- **API Version:** 1.0
- **Last Updated:** Current
- **Framework:** Spring Boot 3.4.4

## Database Schema

### Core Tables
- **teams_user**: Microsoft Teams user information and synchronization status
- **meeting_event**: Meeting event details from Microsoft Graph with transcript status
- **meeting_attendee**: Meeting participant information linked to events
- **meeting_transcript**: Meeting transcript content and metadata
- **meeting_instance**: Individual meeting instances for recurring meetings
- **meeting_url_access**: Meeting URL sharing permissions and access control
- **graph_user_subscription**: Microsoft Graph API subscriptions per user

### Key Relationships
- Meeting events can have multiple attendees and transcripts
- Meeting instances track individual occurrences of recurring meetings
- Users can share meeting URLs with other users through access control
- Graph subscriptions are managed per user for webhook lifecycle
- Teams users are synchronized and linked to meeting participants

## Configuration

### Azure AD Configuration
1. Register an application in Azure AD
2. Grant Microsoft Graph API permissions:
   - `Calendars.Read`
   - `OnlineMeetings.Read`
   - `User.Read.All`
3. Configure webhook endpoints for real-time notifications

### RabbitMQ Configuration
The application uses message queues for asynchronous processing:
- **Event Queue**: For Microsoft Graph meeting event processing
- **Transcript Queue**: For meeting transcript processing and AI summarization
- **Management UI**: Available at http://localhost:15672 for monitoring

### Redis Configuration
- Caching for transcript content and URL mappings
- Deduplication for webhook messages
- Session management and temporary data storage

## Development

### Project Structure
```
src/main/java/com/peaknote/demo/
├── config/              # Configuration classes
│   ├── AIConfig.java
│   ├── AzureProperties.java
│   ├── CacheConfig.java
│   ├── GlobalExceptionHandler.java
│   ├── GraphClientConfig.java
│   ├── RabbitMQConfig.java
│   ├── RedisConfig.java
│   └── WebhookProperties.java
├── controller/          # REST API controllers
│   ├── AttendeeController.java
│   ├── DebugController.java
│   ├── MailController.java
│   ├── TranscriptController.java
│   └── WebhookController.java
├── entity/              # JPA entities
│   ├── GraphUserSubscription.java
│   ├── MeetingAttendee.java
│   ├── MeetingEvent.java
│   ├── MeetingTranscript.java
│   ├── MeetingUrlAccess.java
│   └── TeamsUser.java
├── service/             # Business logic services
│   ├── GraphService.java
│   ├── MeetingSummaryService.java
│   ├── SubscriptionService.java
│   ├── TeamsUserSyncService.java
│   ├── TranscriptService.java
│   └── MailService.java
├── repository/          # Data access layer
├── dto/                 # Data transfer objects
├── exception/           # Custom exceptions
└── util/                # Utility classes
```

### Key Services
- **GraphService**: Microsoft Graph API integration and authentication
- **TranscriptService**: Transcript processing and management
- **MeetingSummaryService**: AI-powered meeting summarization using Llama 3.3
- **SubscriptionService**: Graph API subscription lifecycle management
- **TeamsUserSyncService**: Automated user synchronization from Microsoft Teams
- **MailService**: Email notifications and PDF report generation
- **MessageConsumer**: Asynchronous message processing via RabbitMQ
- **MessageProducer**: Message queue publishing
- **PayloadParserService**: Webhook payload parsing and validation

### Adding New Features
1. Create entity classes in `entity/` package
2. Add repository interfaces in `repository/` package
3. Implement business logic in `service/` package
4. Create REST endpoints in `controller/` package
5. Update configuration as needed

## Deployment

### Docker Deployment
The project includes `docker-compose.yml` for easy local development:

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: 123456
      MYSQL_DATABASE: peaknote
    ports:
      - "3306:3306"
  
  redis:
    image: redis:7
    ports:
      - "6379:6379"
  
  rabbitmq:
    image: rabbitmq:3-management
    ports:
      - "5672:5672"
      - "15672:15672"
```

### Production Dockerfile
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Production Considerations
- Use external database (Azure SQL, AWS RDS)
- Configure Redis cluster for high availability
- Set up RabbitMQ cluster for message reliability
- Implement proper logging and monitoring
- Configure SSL/TLS for webhook endpoints
- Set up health checks and metrics

## Monitoring and Logging

### Health Checks
- Database connectivity
- Redis connection status
- RabbitMQ connection status
- Microsoft Graph API connectivity

### Key Metrics
- Webhook processing latency
- Transcript processing success rate
- AI summarization performance
- Database query performance

### Logging
The application uses SLF4J with structured logging:
- Webhook processing events
- Transcript processing status
- Error tracking and debugging
- Performance metrics

## Security

### Data Protection
- All sensitive data encrypted at rest
- Secure communication with Microsoft Graph API
- Access control for meeting URL sharing
- Audit trail for all operations

### Authentication
- Azure AD integration for user authentication
- Client secret-based service authentication
- Webhook validation for Microsoft Graph

## Troubleshooting

### Common Issues

#### Webhook Not Receiving Events
1. Verify Azure AD app registration permissions
2. Check webhook URL accessibility
3. Validate subscription status in Graph API
4. Review application logs for errors

#### Transcript Processing Failures
1. Check OpenAI API key configuration
2. Verify meeting event status in database
3. Review RabbitMQ queue status
4. Check Redis connectivity

#### Database Connection Issues
1. Verify MySQL server status
2. Check connection pool configuration
3. Review database credentials
4. Monitor connection limits

### Debug Mode
Enable debug logging by adding to `application.yml`:
```yaml
logging:
  level:
    com.peaknote.demo: DEBUG
    org.springframework.web: DEBUG
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

### Code Style
- Follow Java coding conventions
- Use meaningful variable and method names
- Add comprehensive comments for complex logic
- Include unit tests for all new features

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Create an issue in the GitHub repository
- Contact the development team
- Review the documentation and troubleshooting guide

## Roadmap

### Upcoming Features
- [ ] Advanced meeting analytics
- [ ] Integration with additional calendar systems
- [ ] Enhanced AI summarization capabilities
- [ ] Mobile application support
- [ ] Advanced reporting and dashboards

### Version History
- **v1.0.0**: Initial release with core meeting intelligence features
- **v1.1.0**: Enhanced AI summarization and performance improvements
- [ ] Advanced webhook processing and error handling

---

**PeakNote** - Transforming meetings into actionable intelligence
