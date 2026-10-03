package com.jarvis.core;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SystemOrchestrator {

    private final HttpClient httpClient;
    private final ExecutorService threadPool;
    private static final String PYTHON_SERVICE_URL = "http://localhost:8000/api/v1/quantum/simulate";

    public SystemOrchestrator() {
        this.threadPool = Executors.newFixedThreadPool(10);
        this.httpClient = HttpClient.newBuilder()
                .executor(threadPool)
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public CompletableFuture<String> dispatchQuantumTask(String command, int qubits) {
        String jsonPayload = String.format("{\"command\": \"%s\", \"num_qubits\": %d}", command, qubits);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PYTHON_SERVICE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        System.out.println("[Java Orchestrator] Routing task to Python Quantum Engine...");

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .exceptionally(ex -> "[Java Error] Failed to reach Python engine: " + ex.getMessage());
    }

    public static void main(String[] args) {
        System.out.println("=== JARVIS Java Enterprise Core Online ===");
        SystemOrchestrator orchestrator = new SystemOrchestrator();

        // Asynchronously request a quantum execution
        orchestrator.dispatchQuantumTask("Execute optimization search", 4)
                .thenAccept(response -> {
                    System.out.println("[Java Orchestrator] Received Execution Payload:");
                    System.out.println(response);
                })
                .join();
    }
}
