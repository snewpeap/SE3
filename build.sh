./mvnw clean package -pl backend-main -am -Dmaven.test.skip=true
./mvnw clean package -pl batch -am -Dmaven.test.skip=true
cp backend-main/target/se3.jar backend-main/docker/
cp batch/target/se3.jar batch/docker/