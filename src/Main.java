
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Error: Please provide a GitHub username.");
            System.out.println("Usage: java Main <username>");
            return;
        }

        String username = args[0];

        String url = "https://api.github.com/users/" + username + "/events";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/vnd.github+json")
                .GET()
                .build();

        HttpResponse<String> response;
        try {
           response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println("Error: Could not connect to GitHub. Please check your internet connection.");
            return;
        }

        if (response.statusCode() == 404) {
            System.out.println("Error: User '" + username + "' not found on GitHub.");
            return;
        }
        if (response.statusCode() == 403) {
            System.out.println("Error: GitHub API rate limit exceeded. Please try again later.");
            return;
        }

        if (response.statusCode() != 200) {
            System.out.println("Error: Unexpected response from GitHub (status " + response.statusCode() + ").");
            return;
        }

        String json = response.body();
        if (json.equals("[]")) {
            System.out.println("No recent public activity found for '" + username + "'.");
            return;
        }
        int currentIndex = 0;
        while(true) {
            String type = extractValue(json, "type", currentIndex);
            if(type == null) {
                break;
            }

            int typeLocation = json.indexOf("\"type\":\"" + type + "\"", currentIndex);
            String repoName = extractValue(json, "name", typeLocation);
            System.out.println(formatEvent(type, repoName));

            // Move past this event so we find the NEXT one on the next loop
            currentIndex = typeLocation + 1;
        }
    }
    private static String extractValue(String json, String key, int fromIndex) {
        String pattern = "\"" + key + "\":\"";
        int startIndex = json.indexOf(pattern, fromIndex);
        if (startIndex == -1) {
            return null; // key not found
        }
        startIndex += pattern.length(); // move past the pattern itself
        int endIndex = json.indexOf("\"", startIndex);
        return json.substring(startIndex, endIndex);
    }

    private  static  String formatEvent( String type, String repoName) {
        switch (type) {
            case "PushEvent":
                return "Pushed to " + repoName;
            case "WatchEvent":
                return "Starred " + repoName;
            case "CreateEvent":
                return "Created a new branch/repo in " + repoName;
            case "IssuesEvent":
                return "Opened or updated asn issue in " + repoName;
            case "PullRequestEvent":
                return "Opened or updated a pull request in " + repoName;
            case "ForkEvent":
                return "Forked " + repoName;
            default:
                return type + " on " + repoName;
        }
    }
}