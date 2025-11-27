.PHONY: build run test clean stop logs help

# Default target
.DEFAULT_GOAL := help

# Build the application
build:
	@echo "Building the application..."
	./mvnw clean package -DskipTests
	@echo "Build completed successfully!"

# Run the application with Docker Compose
run:
	@echo "Starting the application with Docker Compose..."
	docker-compose up --build -d
	@echo "Application is running!"
	@echo "Access the API at: http://localhost:8080/api/v1/deals"
	@echo "Access Swagger UI at: http://localhost:8080/api/v1/swagger-ui.html"

# Run tests
test:
	@echo "Running tests..."
	./mvnw test
	@echo "Tests completed!"

# Run tests with coverage report
test-coverage:
	@echo "Running tests with coverage..."
	./mvnw clean test jacoco:report
	@echo "Coverage report generated at: target/site/jacoco/index.html"

# Stop the application
stop:
	@echo "Stopping the application..."
	docker-compose down
	@echo "Application stopped!"

# Clean containers, images, and volumes
clean:
	@echo "Cleaning up..."
	docker-compose down -v --rmi local
	./mvnw clean
	@echo "Cleanup completed!"

# View logs
logs:
	docker-compose logs -f fx-deals-app

# View database logs
logs-db:
	docker-compose logs -f postgres

# Restart the application
restart: stop run

# Run locally (without Docker)
run-local:
	@echo "Running application locally..."
	./mvnw spring-boot:run

# Help
help:
	@echo "Available targets:"
	@echo "  make build          - Build the application"
	@echo "  make run            - Run the application with Docker Compose"
	@echo "  make test           - Run unit tests"
	@echo "  make test-coverage  - Run tests with coverage report"
	@echo "  make stop           - Stop the application"
	@echo "  make clean          - Clean containers, images, and build artifacts"
	@echo "  make logs           - View application logs"
	@echo "  make logs-db        - View database logs"
	@echo "  make restart        - Restart the application"
	@echo "  make run-local      - Run the application locally (without Docker)"
	@echo "  make help           - Show this help message"