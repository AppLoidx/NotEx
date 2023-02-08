mvn clean verify sonar:sonar \
  -Dsonar.projectKey=NotExComparing \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=sqp_89b11ca7033789c83ed6edfffa83fc739160b728
  -Dsonar.tests=./src/test
