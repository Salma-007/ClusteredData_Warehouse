.PHONY: build run test clean stop logs help

.DEFAULT_GOAL := help

build:
	@echo "Building the application..."
	./mvnw clean package -DskipTests
	@echo "Build completed successfully!"

run:
	@echo "Starting the application with Docker Compose..."
	docker-compose up --build -d
	@echo "Application is running!"
	@echo "Access the API at: http://localhost:8080/api/deals"

test:
	@echo "Running tests..."
	./mvnw test
	@echo "Tests completed!"

test-coverage:
	@echo "Running tests with coverage..."
	./mvnw clean test jacoco:report
	@echo "Coverage report generated at: target/site/jacoco/index.html"

stop:
	@echo "Stopping the application..."
	docker-compose down
	@echo "Application stopped!"

clean:
	@echo "Cleaning up..."
	docker-compose down -v --rmi local
	./mvnw clean
	@echo "Cleanup completed!"

logs:
	docker-compose logs -f fx-deals-app

logs-db:
	docker-compose logs -f postgres

restart: stop run

run-local:
	@echo "Running application locally..."
	./mvnw spring-boot:run

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