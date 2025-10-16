# 1. BUILD STAGE: ใช้ Maven Image ที่มี JDK สำหรับ Build แอป
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy ไฟล์ POM และรันการดาวน์โหลด dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code ที่เหลือ และ Build
COPY src ./src
RUN mvn clean package -DskipTests

# 2. RUNTIME STAGE: ใช้ JRE Image ที่เล็กกว่าสำหรับรัน
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy ไฟล์ .jar ที่ Build เสร็จแล้วจาก build stage
# *ต้องแน่ใจว่าชื่อไฟล์ .jar ที่ได้ตรงกับที่ถูกสร้าง
COPY --from=build /app/target/*.jar app.jar

# ตั้งค่าพอร์ตและ Start Command
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]