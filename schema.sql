CREATE DATABASE IF NOT EXISTS jarvis_system_db;
USE jarvis_system_db;

-- System Memory Store
CREATE TABLE IF NOT EXISTS cognitive_memory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id VARCHAR(64) NOT NULL,
    user_input TEXT NOT NULL,
    ai_response TEXT NOT NULL,
    intent_tag VARCHAR(50),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_session (session_id)
) ENGINE=InnoDB;

-- Quantum Execution History
CREATE TABLE IF NOT EXISTS quantum_job_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    num_qubits INT NOT NULL,
    measurement_results JSON NOT NULL,
    execution_time_ms FLOAT NOT NULL,
    executed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;
