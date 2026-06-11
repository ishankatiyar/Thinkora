# Thinkoraa - AI Research Assistant

A full-stack AI-powered research assistant that runs as a Chrome browser extension. It analyzes the content of any web page using **Google Gemini AI** and provides instant summaries and research suggestions - all from a clean side panel inside your browser.

---

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Setup and Installation](#setup-and-installation)
  - [1. Backend (Spring Boot)](#1-backend-spring-boot)
  - [2. Chrome Extension](#2-chrome-extension)
- [Configuration](#configuration)
- [API Reference](#api-reference)
- [How It Works](#how-it-works)
- [Security Notes](#security-notes)
- [Known Limitations](#known-limitations)

---

## Overview

Thinkoraa is a two-part application:

1. **Backend** - A Java Spring Boot REST API that receives page content from the extension, constructs a prompt, calls the Google Gemini API, and returns the AI-generated result.
2. **Chrome Extension** - A Manifest V3 browser extension with a side panel UI where users click a button to summarize the current page or get research suggestions.

---

## Architecture

`
Browser (Chrome Extension)
        |
        |  POST /api/research/process
        |  { content: "...", operation: "summarize" or "suggest" }
        v
Spring Boot Backend (localhost:8080)
        |
        |  POST https://generativelanguage.googleapis.com/...
        v
Google Gemini API
        |
        |  Generated text response
        v
Chrome Extension (Side Panel UI)
`

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend Language | Java 21 |
| Backend Framework | Spring Boot 4.0.6 |
| HTTP Client | Spring WebClient (WebFlux) |
| Build Tool | Maven |
| Code Generation | Lombok |
| AI Provider | Google Gemini API (gemini-2.5-flash) |
| Frontend | Chrome Extension (Manifest V3) |
| Extension UI | HTML5, CSS3, Vanilla JavaScript |

---

## Prerequisites

Before you begin, make sure you have the following installed:

- **Java 21** - https://www.oracle.com/java/technologies/downloads/#java21
- **Maven 3.8+** - https://maven.apache.org/download.cgi (or use the included mvnw.cmd wrapper - no install needed)
- **Google Chrome** (version 114 or later for side panel support)
- **Google Gemini API Key** - https://aistudio.google.com/app/apikey

---

## Project Structure

`
AI Research Assistantt/
|-- README.md
|
|-- assisstant/                          # Java Spring Boot backend
|   |-- pom.xml                          # Maven dependencies and build config
|   |-- mvnw.cmd                         # Maven wrapper (Windows)
|   +-- src/
|       +-- main/
|           |-- java/com/research/assisstant/
|           |   |-- AssisstantApplication.java     # Spring Boot entry point
|           |   |-- ResearchController.java        # REST API controller
|           |   |-- ResearchService.java           # Gemini API integration
|           |   |-- ResearchRequest.java           # Request DTO
|           |   |-- GeminiResponse.java            # Gemini response DTO
|           |   +-- WebClientConfig.java           # WebClient bean config
|           +-- resources/
|               +-- application.properties         # App configuration
|
+-- extension/                           # Chrome browser extension
    |-- manifest.json                    # Extension manifest (V3)
    |-- background.js                    # Service worker
    |-- sidepanel.html                   # Side panel UI
    |-- sidepanel.css                    # Side panel styles
    |-- sidepanel.js                     # Side panel logic
    +-- icon.png                         # Extension icon
`

---

## Setup and Installation

### 1. Backend (Spring Boot)

#### Step 1 - Configure your Gemini API key

Open assisstant/src/main/resources/application.properties and set your API key:

`properties
spring.application.name=assisstant
gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=
gemini.api.key=YOUR_GEMINI_API_KEY_HERE
`

Replace YOUR_GEMINI_API_KEY_HERE with your actual key from Google AI Studio.

**Recommended:** Use an environment variable instead of hardcoding:

`properties
gemini.api.key=${GEMINI_API_KEY}
`

Then in PowerShell before running:

`powershell
$env:GEMINI_API_KEY = "your-key-here"
`

#### Step 2 - Build the project

Navigate to the assisstant directory and build:

`powershell
cd "c:\ishan\AI Research Assistantt\assisstant"
.\mvnw.cmd clean install
`

Or if Maven is installed globally:

`powershell
mvn clean install
`

#### Step 3 - Run the backend

`powershell
.\mvnw.cmd spring-boot:run
`

Or run the compiled JAR:

`powershell
java -jar target\assisstant-0.0.1-SNAPSHOT.jar
`

The backend starts at **http://localhost:8080**.

You should see output like:
`
Started AssisstantApplication in X.XXX seconds
`

---

### 2. Chrome Extension

#### Step 1 - Open Chrome Extensions

Navigate to:
`
chrome://extensions
`

#### Step 2 - Enable Developer Mode

Toggle **Developer mode** on using the switch in the top-right corner of the page.

#### Step 3 - Load the extension

1. Click **Load unpacked**
2. Select the extension folder: c:\ishan\AI Research Assistantt\extension\
3. Click **Select Folder**

The Research Assistant extension will appear in your extensions list.

#### Step 4 - Pin the extension (optional)

Click the puzzle piece icon in the Chrome toolbar, find Research Assistant, and click the pin icon.

#### Step 5 - Use it

1. Make sure the **backend is running** (Step 3 above)
2. Open any web page you want to research
3. Click the **Research Assistant** icon in your toolbar
4. The side panel opens - click **Summarize Page**
5. The AI-generated result appears within a few seconds

---

## Configuration

All backend configuration lives in assisstant/src/main/resources/application.properties.

| Property | Description |
|---|---|
| spring.application.name | Application name used internally by Spring |
| gemini.api.url | Gemini API endpoint URL (your key is appended at runtime) |
| gemini.api.key | Your Google Gemini API key |

To change the server port from the default 8080, add:

`properties
server.port=9090
`

Note: If you change the port, also update host_permissions in extension/manifest.json to match.

---

## API Reference

### POST /api/research/process

Processes web page content using AI and returns a result.

**Request Body (JSON):**

`json
{
  "content": "The text content of the web page to analyze",
  "operation": "summarize"
}
`

| Field | Type | Required | Description |
|---|---|---|---|
| content | string | Yes | The page text to process |
| operation | string | Yes | Either summarize or suggest |

**Supported Operations:**

| Operation | What it does |
|---|---|
| summarize | Returns a concise summary of the page content |
| suggest | Returns research suggestions and follow-up questions |

**Response:** HTTP 200 with a plain text string containing the AI-generated result.

**Example using PowerShell:**

`powershell
Invoke-RestMethod -Method POST `
  -Uri "http://localhost:8080/api/research/process" `
  -ContentType "application/json" `
  -Body '{"content": "Spring Boot is a Java framework...", "operation": "summarize"}'
`

---

## How It Works

1. User clicks the extension icon - Chrome opens the **side panel**.
2. User clicks **Summarize Page** or another action button.
3. sidepanel.js extracts visible text from the active tab using the Chrome scripting API.
4. It sends a POST request to http://localhost:8080/api/research/process with the content and operation.
5. ResearchController receives the request and delegates to ResearchService.
6. ResearchService builds an AI prompt and calls the **Google Gemini API** via Spring WebClient.
7. The Gemini response is parsed from candidates[0].content.parts[0].text and returned as plain text.
8. The result is displayed in the side panel result card.
9. Users can also write and save **Research Notes** (up to 500 characters) in the panel.

---
