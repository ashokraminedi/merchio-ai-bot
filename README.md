# Merchio 🛍️

> Your Intelligent Shopping Companion

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen.svg)](https://spring.io/projects/spring-boot)

AI-powered chatbot for e-commerce platforms. Built with Spring Boot, PostgreSQL, and integrated with multiple AI providers (OpenAI, Claude, Azure).

## ✨ Features

- 🤖 **Multi-AI Support** - OpenAI GPT-4, Claude Sonnet, Azure OpenAI
- 📦 **Order Tracking** - Real-time order status and delivery updates
- 🔍 **Smart Product Search** - Natural language product catalog search
- ✅ **Inventory Management** - Real-time stock availability checking
- 💳 **Payment Support** - Payment inquiries and refund assistance
- 🧠 **Intent Detection** - Rule-based intelligent prompt matching
- 🔌 **RESTful API** - Clean API design for easy integration

## 🛠️ Tech Stack

| Component | Technology |
|-----------|------------|
| **Backend** | Java 21, Spring Boot 3.2 |
| **Database** | PostgreSQL 15+ |
| **AI Providers** | OpenAI GPT-4, Claude Sonnet 4.5, Azure OpenAI |
| **Testing** | JUnit 5, Mockito |
| **Build Tool** | Maven 3.8+ |

## 📋 Prerequisites

- ☕ Java 21 or higher
- 🐘 PostgreSQL 15 or higher
- 📦 Maven 3.8 or higher
- 🔑 OpenAI API Key (or Claude/Azure API Key)

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/ashokraminedi/merchio.git
cd merchio
```

### 2. Setup Database
```bash
# Using Docker
docker run --name merchio-db \
  -e POSTGRES_PASSWORD=your_password \
  -e POSTGRES_DB=merchio_db \
  -p 5432:5432 \
  -d postgres:15

# Or install PostgreSQL locally and create database
createdb merchio_db
```

### 3. Configure Environment Variables

Create `.env` file in project root:
```env
OPENAI_API_KEY=sk-your-openai-key
CLAUDE_API_KEY=sk-ant-your-claude-key
POSTGRES_PASSWORD=your_password
```

### 4. Build and Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### 5. Access the API

- **Chatbot Endpoint:** `http://localhost:8080/api/chatbot/message`
- **Health Check:** `http://localhost:8080/api/chatbot/health`

## 📖 API Documentation

### Send Message to Merchio

**Endpoint:** `POST /api/chatbot/message`

**Request:**
```json
{
  "message": "What's the status of order ORD001ABC?",
  "sessionId": "user-session-123",
  "conversationHistory": []
}
```

**Response:**
```json
{
  "message": "Your order #ORD001ABC has been shipped and is on its way! Expected delivery: 2-3 business days.",
  "intent": "ORDER_STATUS",
  "confidence": 0.95,
  "entities": {
    "orderNumber": "ORD001ABC"
  },
  "provider": "OpenAI"
}
```

### Health Check

**Endpoint:** `GET /api/chatbot/health`

**Response:**
```json
{
  "message": "Merchio chatbot is running",
  "status": "healthy",
  "version": "0.0.1"
}
```

## 🧪 Testing

Run all tests:
```bash
mvn test
```

Run specific test:
```bash
mvn test -Dtest=ChatbotServiceTest
```

## 🔄 Switching AI Providers

Edit `application.yml`:
```yaml
ai:
  provider: openai  # Options: openai, claude, azure
```

The system automatically uses the configured provider through Spring's conditional configuration.

## 🏗️ Architecture
```
User Request → Intent Detection → Database Query → AI Processing → Response
```

**Flow:**
1. **Intent Detection** - Matches user prompt against predefined rules
2. **Entity Extraction** - Extracts order numbers, product names, emails
3. **Context Gathering** - Queries database for relevant information
4. **AI Generation** - Generates natural language response
5. **Response Delivery** - Returns formatted chatbot response

## 📁 Project Structure
```
merchio/
├── src/main/java/com/merchio/
│   ├── MerchioApplication.java       # Main application
│   ├── ai/                           # AI provider interfaces
│   ├── controller/                   # REST controllers
│   ├── entity/                       # JPA entities
│   ├── repository/                   # Data repositories
│   └── service/                      # Business logic
├── src/main/resources/
│   ├── application.yml               # Configuration
│   ├── chatbot-rules.json           # Intent rules
│   └── data.sql                     # Sample data
└── src/test/                        # Tests
```


## 👤 Author

**Your Name**
- GitHub: [@ashokraminedi](https://github.com/ashokraminedi)
- LinkedIn: [Ashok Kumar Raminedi](https://linkedin.com/in/ashokraminedi)

## 🙏 Acknowledgments

- Spring Boot Team
- OpenAI & Anthropic for AI APIs
- All contributors

---
