# java-spring-aws-sqs

## Running Postgres
docker run --name postgresdb -p 5432:5432 -e POSTGRES_PASSWORD=postgres -d postgres

## Running the Application
AWS_KEY=<aws_key> AWS_SECRET=<aws_secret> AWS_TOKEN=<aws_token> ./gradlew bootRun

## Running the Application in debug mode
AWS_KEY=<aws_key> AWS_SECRET=<aws_secret> AWS_TOKEN=<aws_token> ./gradlew bootRun --debug-jvm
