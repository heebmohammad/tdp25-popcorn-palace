# Instructions

## Prerequisites

Before you begin, ensure you have met the following requirements:

- **Operating System**: [Windows, Linux, macOS]
- **Software Requirements**:
  - **Java SDK 21**: [Download Java SDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
  - **Docker Desktop**: [Download Docker Desktop](https://www.docker.com/products/docker-desktop/)
  - **Git**: [Install Git](https://git-scm.com/)

## Setup

1. **Download and Install Dependencies**:
   - **Java SDK 21**: Download and install Java SDK 21 from [Oracle’s official website](https://www.oracle.com/java/technologies/downloads/#java21).
   - **Docker Desktop**: Download and install Docker from [Docker’s official website](https://www.docker.com/products/docker-desktop/).
   - **Git**: Ensure Git is installed by running `git --version` in the terminal. If not installed, follow the link above to install it.

2. **Clone the Repository**:
    ```bash
    git clone git@github.com:heebmohammad/tdp25-popcorn-palace.git
    cd tdp25-popcorn-palace
    ```

## Build

1. **Ensure Docker Desktop is Running**:  
   Ensure Docker Desktop (engine) is running before proceeding.

2. **Run Docker Compose**:  
   In the project directory, where the `compose.yml` file is located, run:
    ```bash
    docker-compose up
    ```
   This command will start a local PostgreSQL instance using Docker.

3. **Database Configuration**:
   - Database properties (e.g., URL, username, password) can be found in the file:
     ```
     tdp-2025-homework/popcorn-palace/src/main/resources/application.yaml
     ```
   - The database schema can be found in the file:
     ```
     tdp-2025-homework/popcorn-palace/src/main/resources/schema.sql
     ```
   - If you need sample data, uncomment the relevant section in:
     ```
     tdp-2025-homework/popcorn-palace/src/main/resources/data.sql
     ```

4. **Database Management (Optional)**:  
   You can manage the PostgreSQL database using **pgAdmin** or any PostgreSQL client.

## Run

To run the application locally:

- **On Windows**:
    ```bash
    ./mvnw.cmd spring-boot:run
    ```

- **On Linux**:
    ```bash
    ./mvnw spring-boot:run
    ```

Once the application is running, visit `http://localhost:8080` (or any port specified in the configuration) to access the app.

## Test

To run the tests for this project:

- **On Windows**:
    ```bash
    ./mvnw.cmd test
    ```

- **On Linux**:
    ```bash
    ./mvnw test
    ```

This will run all the tests defined in the project.

> **Note**: The Thunder Client extension in VS Code is a handy tool for testing APIs.
