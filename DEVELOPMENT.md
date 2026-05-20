# Development Setup Guide

## Prerequisites

### System Requirements
- Windows, macOS, or Linux
- 8GB RAM minimum
- 20GB free disk space

### Software Requirements

#### For Backend
- Java 21 JDK
  - Download: https://jdk.java.net/21/
  - Set JAVA_HOME environment variable
- Maven 3.8.0+
  - Download: https://maven.apache.org/download.cgi
- MySQL 8.0+
  - Download: https://dev.mysql.com/downloads/mysql/
  - Or use Docker: `docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password mysql:8.0`

#### For Frontend
- Node.js 18.0+
  - Download: https://nodejs.org/
- npm 9.0+
  - Comes with Node.js

#### For Mobile
- Flutter 3.0+
  - Download: https://flutter.dev/docs/get-started/install
  - Add to PATH
- Android Studio with Android SDK 21+
  - Download: https://developer.android.com/studio

---

## Step 1: Clone/Setup Repository

```bash
# Navigate to project root
cd traffic-fine-system

# View directory structure
ls -la
```

---

## Step 2: Database Setup

### Option A: Using MySQL directly

```bash
# Start MySQL server
mysql -u root -p

# Create database
CREATE DATABASE traffic_fine_db;
USE traffic_fine_db;

# Import initial data
source backend/src/main/resources/init.sql;

# Verify
SHOW TABLES;
SELECT COUNT(*) FROM fine_categories;
```

### Option B: Using Docker

```bash
# Start MySQL container
docker run -d \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=traffic_fine_db \
  --name traffic-fine-mysql \
  mysql:8.0

# Wait for container to start (20 seconds)
sleep 20

# Copy init script into container
docker cp backend/src/main/resources/init.sql traffic-fine-mysql:/init.sql

# Execute init script
docker exec -i traffic-fine-mysql mysql -uroot -proot traffic_fine_db < backend/src/main/resources/init.sql
```

---

## Step 3: Backend Setup

### Navigate to Backend
```bash
cd backend
```

### Configure Application
Edit `src/main/resources/application-dev.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/traffic_fine_db
    username: root
    password: 
```

Or create `.env.local`:
```bash
DB_HOST=localhost
DB_PORT=3306
DB_NAME=traffic_fine_db
DB_USER=root
DB_PASSWORD=
JWT_SECRET=my-super-secret-key-that-is-at-least-256-bits-long
```

### Build Project
```bash
mvn clean install
```

### Run Spring Boot
```bash
mvn spring-boot:run
```

Server starts on `http://localhost:8080`

### Verify Backend
```bash
curl http://localhost:8080/api/fines/reference/test
# Should return: {"success":false,"message":"Fine not found",...}
```

---

## Step 4: Frontend Payment Portal

### Navigate to Frontend
```bash
cd ../frontend/payment-portal
```

### Install Dependencies
```bash
npm install
```

### Configure API
Edit `.env.local`:
```
VITE_API_URL=http://localhost:8080/api
```

### Start Development Server
```bash
npm run dev
```

Portal runs on `http://localhost:3000`

### Test Login
- Email: `driver@example.com`
- Password: `Driver@123`

---

## Step 5: Admin Dashboard

### Navigate to Admin Dashboard
```bash
cd ../admin-dashboard
```

### Install Dependencies
```bash
npm install
```

### Configure API
Edit `.env.local`:
```
VITE_API_URL=http://localhost:8080/api
```

### Start Development Server
```bash
npm run dev
```

Dashboard runs on `http://localhost:3001`

### Test Login
- Email: `admin@slpolice.lk`
- Password: (needs to be set during user creation)

---

## Step 6: Mobile App (Optional)

### Navigate to Mobile
```bash
cd ../../mobile/flutter_app
```

### Install Dependencies
```bash
flutter pub get
```

### Configure API
Edit `lib/services/api_service.dart`:
```dart
static const String baseUrl = 'http://YOUR_COMPUTER_IP:8080/api';
```

### Run on Emulator
```bash
# Start Android emulator first, then:
flutter run
```

### Run on Physical Device
```bash
# Connect Android device via USB
flutter devices  # Verify device is listed
flutter run
```

---

## Step 7: Test API Endpoints

### Using cURL

#### 1. Login (Get Token)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"driver@example.com","password":"Driver@123"}'

# Response will include token like:
# {"success":true,"data":{"token":"eyJhbGc...","user":{...}}}
```

#### 2. Search Fine
```bash
curl -X GET http://localhost:8080/api/fines/reference/FIN1699500000123ABC
```

#### 3. Get Dashboard Stats (Admin)
```bash
curl -X GET http://localhost:8080/api/admin/dashboard \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

### Using Postman

1. Import collection: `docs/postman-collection.json`
2. Replace `YOUR_TOKEN_HERE` with actual token from login
3. Run requests

---

## Development Workflow

### Backend Development
```bash
# Terminal 1: Keep backend running
cd backend
mvn spring-boot:run

# Terminal 2: Make changes and rebuild
# Changes to controllers auto-reload via devtools
# For entity changes, restart the app
```

### Frontend Development
```bash
# Terminal 1: Keep payment portal running
cd frontend/payment-portal
npm run dev

# Terminal 2: Keep admin dashboard running
cd frontend/admin-dashboard
npm run dev

# Hot reload on file save
```

### Mobile Development
```bash
# Terminal 1: Keep app running
cd mobile/flutter_app
flutter run -v  # verbose for debugging

# Terminal 2: Make changes
# Hot reload: Press 'r' in terminal
# Full restart: Press 'R' in terminal
```

---

## Common Issues & Solutions

### Issue: MySQL Connection Failed
```
com.mysql.cj.jdbc.exceptions.CommunicationsException
```
**Solution:**
- Verify MySQL is running: `mysql -u root -p`
- Check connection string in application-dev.yml
- Verify database exists: `SHOW DATABASES;`

### Issue: Port 8080 Already in Use
```bash
# Find process using port 8080
lsof -i :8080  # macOS/Linux
netstat -ano | findstr :8080  # Windows

# Kill process
kill -9 PID  # macOS/Linux
taskkill /PID PID /F  # Windows

# Or change port in application.yml:
server:
  port: 8081
```

### Issue: Node modules issues
```bash
# Clear cache and reinstall
npm cache clean --force
rm -rf node_modules package-lock.json
npm install
```

### Issue: Flutter build fails
```bash
# Clean and rebuild
flutter clean
flutter pub get
flutter run --no-fast-start
```

---

## Testing the Complete Flow

### 1. Start All Services
```bash
# Terminal 1: Backend
cd backend
mvn spring-boot:run

# Terminal 2: Payment Portal
cd frontend/payment-portal
npm run dev

# Terminal 3: Admin Dashboard
cd frontend/admin-dashboard
npm run dev
```

### 2. Login as Driver
- Visit: `http://localhost:3000`
- Email: `driver@example.com`
- Password: `Driver@123`

### 3. Search for Fine
- Reference: Any valid fine reference from database
- System shows fine details

### 4. Pay Fine
- Click "Pay Fine"
- Complete payment flow

### 5. View Admin Dashboard
- Visit: `http://localhost:3001`
- Email: `admin@slpolice.lk`
- View collections and analytics

---

## Performance Tips

1. **Database**: Create indexes on frequently queried columns
2. **Backend**: Use `mvn clean install` to build optimized JAR
3. **Frontend**: Use Chrome DevTools to profile performance
4. **Mobile**: Build release APK for better performance

---

## IDE Setup

### IntelliJ IDEA (Recommended for Java)
1. Open `backend` folder as project
2. Configure SDK: File → Project Structure → SDK → Java 21
3. Install MySQL driver (automatically prompted)
4. Run `TrafficFineSystemApplication.java`

### VS Code (For React)
```bash
# Install extensions:
# - ES7+ React/Redux/React-Native snippets
# - Prettier - Code formatter
# - Tailwind CSS IntelliSense
```

### Android Studio (For Flutter)
```bash
# Install plugins:
# - Flutter
# - Dart
# Configure device emulator via AVD Manager
```

---

## Git Workflow

```bash
# Create feature branch
git checkout -b feature/fine-search

# Make changes
git add .
git commit -m "feat: add fine search functionality"

# Push to remote
git push origin feature/fine-search

# Create pull request on GitHub
```

---

## Debugging

### Backend Debugging
```bash
# Add to IntelliJ: Run → Edit Configurations
# Enable debug mode and set breakpoints
```

### Frontend Debugging
```bash
# Open browser DevTools: F12
# React DevTools browser extension recommended
```

### Mobile Debugging
```bash
flutter run -v  # Verbose output
# Debug console shows logs and errors
```

---

## Next Steps

1. Customize email templates
2. Integrate with actual SMS provider
3. Add payment gateway integration
4. Deploy to production server
5. Configure HTTPS/SSL
6. Setup monitoring and logging
7. Implement caching layer

---

**Happy Coding!** 🚀
