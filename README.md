markdown# GitHub Activity CLI

A command-line tool built in Java that fetches and displays a GitHub user's 
recent public activity directly in the terminal — pushes, stars, new branches, 
and more — without needing to visit their GitHub profile.

Built to practice working with APIs, handling raw JSON without external 
libraries, and building a simple, robust CLI application.

## Features

- Fetches a GitHub user's recent activity via the GitHub REST API
- Parses the JSON response manually — no external libraries or frameworks used
- Displays activity as clean, human-readable lines in the terminal
- Handles errors gracefully: invalid usernames, network failures, rate limits, 
  and users with no recent activity

## Usage

```bash
java Main 
```

### Example

```bash
java Main devonochie
```

**Output:**
Pushed to devonochie/task-tracker-cli
Pushed to devonochie/task-tracker-cli
Created a new branch/repo in devonochie/task-tracker-cli
Pushed to devonochie/SkyStay
Starred devonochie/SkyStay
...

## Error Handling

| Scenario                        | Behavior                                         |
|----------------------------------|---------------------------------------------------|
| No username provided             | Prints usage instructions                          |
| Username doesn't exist           | `Error: User '<username>' not found on GitHub.`    |
| No internet / connection failure | `Error: Could not connect to GitHub...`            |
| API rate limit exceeded          | `Error: GitHub API rate limit exceeded...`         |
| User has no recent activity      | `No recent public activity found for '<username>'.`|

## How It Works

1. Builds a request to GitHub's Events API:  
   `https://api.github.com/users/<username>/events`
2. Sends the request using Java's built-in `HttpClient` (no external HTTP library).
3. Manually parses the raw JSON response as text, extracting each event's 
   `type` and `repo.name` using string search — no JSON library used.
4. Maps each event type (`PushEvent`, `WatchEvent`, `CreateEvent`, etc.) to a 
   friendly, readable sentence.
5. Prints the formatted activity feed to the terminal.

## Constraints

This project intentionally avoids external libraries/frameworks, relying only 
on Java's built-in `java.net.http.HttpClient` and manual string parsing — as 
required by the original project brief.

## Tech Stack

- Java (no external dependencies)

---
*This project is based on the [GitHub User Activity](https://roadmap.sh/projects/github-user-activity) 
challenge from [roadmap.sh](https://roadmap.sh).*
Same naming convention advice as before — I'd suggest github-activity-cli a
