mvn clean compile install -pl com.wudgaby.platform:basis-project -N
mvn clean compile install -pl base-spring-boot-starters -N

mvn clean package -pl basis-project-parent -N
mvn clean compile install -pl basis-project-core -N
mvn flatten:flatten basis-project -N
mvn clean package -pl basis-project-parent -am -amd -Dmaven.test.skip=true