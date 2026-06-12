# Sri Lanka Police Traffic Fine Collection System

Enterprise-grade full-stack application for managing traffic fines and payments across Sri Lanka Police.

## System Overview

### Components
- **Backend**: Spring Boot REST API (Java 21)
- **Web Frontend**: React Single Page Application with Vite
- **Admin Dashboard**: React Admin Dashboard with Analytics
- **Mobile App**: Flutter Android Application
- **Database**: MySQL

### Features
- Traffic police officers issue fines to drivers
- Drivers search and pay fines via web or mobile
- Real-time SMS notifications
- Admin analytics and reporting
- Role-based access control (ADMIN, OFFICER, DRIVER)

---

## Architecture

### Layered Architecture
```
Controller Layer (REST API)
    ↓
Service Layer (Business Logic)
    ↓
Repository Layer (Data Access)
    ↓
Entity Layer (Database Models)
```

### Key Design Patterns
- DTO (Data Transfer Objects) for API communication
- Repository Pattern for data access
- Service Layer for business logic
- JWT stateless authentication
- Role-based authorization
- Centralized exception handling

---

## Backend Setup

### Requirements
- Java 21+
- Maven 3.8+
- MySQL 8.0+

### Installation

1. **Navigate to backend directory**
```bash
cd backend
```

2. **Configure database** (update `application-dev.yml` or `.env.local`)
```yaml
DB_HOST: localhost
DB_PORT: 3306
DB_NAME: traffic_fine_db
DB_USER: root
DB_PASSWORD: 
```

3. **Build project**
```bash
mvn clean install
```

4. **Run application**
```bash
mvn spring-boot:run
```

Server runs on `http://localhost:8080`

### Database Schema
- `users` - Store user data (ADMIN, OFFICER, DRIVER roles)
- `fine_categories` - Traffic fine categories with amounts
- `traffic_fines` - Issued fines with status tracking
- `payments` - Payment transactions

### Running init.sql
Execute `src/main/resources/init.sql` to populate initial data:
- Admin user: admin@slpolice.lk
- Officer user: officer@slpolice.lk
- Sample driver: driver@example.com

---

## Frontend Setup

### Payment Portal

```bash
cd frontend/payment-portal
npm install
npm run dev
```

Runs on `http://localhost:3000`

**Features:**
- Driver login/registration
- Search fines by reference
- View fine details
- Pay fines
- Payment history

### Admin Dashboard

```bash
cd frontend/admin-dashboard
npm install
npm run dev
```

Runs on `http://localhost:3001`

**Features:**
- Dashboard statistics
- District-wise collections
- Category-wise breakdown
- Charts and analytics
- Real-time data

---

## Mobile App Setup

### Requirements
- Flutter 3.0+
- Android SDK 21+

### Installation

```bash
cd mobile/flutter_app
flutter pub get
flutter run
```

**Features:**
- Login
- Search fines
- Pay fines
- Payment confirmation

---

## API Endpoints

### Authentication
```
POST   /api/auth/login       - Login user
POST   /api/auth/register    - Register new driver
```

### Fine Management
```
POST   /api/fines/issue              - Issue new fine (OFFICER)
GET    /api/fines/reference/{ref}    - Get fine details (PUBLIC)
GET    /api/fines/driver/{id}        - Get driver's fines (DRIVER)
GET    /api/fines/officer/{id}       - Get officer's issued fines (OFFICER)
```

### Payments
```
POST   /api/payments                 - Process payment (DRIVER)
GET    /api/payments/history/{id}    - Get payment history (DRIVER)
```

### Admin
```
GET    /api/admin/dashboard              - Dashboard stats (ADMIN)
GET    /api/admin/collections/district   - District collections (ADMIN)
GET    /api/admin/collections/category   - Category collections (ADMIN)
```

---

## API Request Examples

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "driver@example.com",
    "password": "Driver@123"
  }'
```

### Get Fine
```bash
curl -X GET http://localhost:8080/api/fines/reference/FIN1699500000123ABC
```

### Process Payment
```bash
curl -X POST http://localhost:8080/api/payments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "fineId": 1,
    "paymentMethod": "CREDIT_CARD",
    "paymentGatewayReference": "gateway_ref_12345"
  }'
```

---

## Authentication

### JWT Flow
1. User logs in with credentials
2. Backend validates and issues JWT token
3. Client stores token in localStorage
4. Token included in Authorization header: `Bearer {token}`
5. Server validates token on each request

### Roles & Permissions
- **ADMIN**: Full access to all endpoints and analytics
- **OFFICER**: Issue fines, view own fines
- **DRIVER**: View own fines, make payments

---

## Database Models

### User
```
- id (PK)
- email (UNIQUE)
- password (hashed)
- fullName
- phone
- role (ENUM)
- licenseNumber
- vehicleRegistration
- district
- active
- createdAt, updatedAt
```

### TrafficFine
```
- id (PK)
- reference (UNIQUE)
- driver_id (FK)
- officer_id (FK)
- category_id (FK)
- amount
- status (ENUM: PENDING, PAID, CANCELLED)
- location
- vehicleRegistration
- district
- notes
- issuedAt, updatedAt
```

### Payment
```
- id (PK)
- transactionId (UNIQUE)
- fine_id (FK)
- payer_id (FK)
- amount
- paymentMethod (ENUM)
- paymentGatewayReference
- notes
- paidAt
```

### FineCategory
```
- id (PK)
- code (UNIQUE)
- description
- amount
- violationType
- createdAt
```

---

## SMS Service

### Interface
```java
SmsService {
  void sendPaymentConfirmation(String phone, String transactionId, String amount);
  void sendFineNotification(String phone, String fineReference, String amount);
  void sendOfficerNotification(String phone, String fineReference, String amount);
}
```

### Current Implementation
Mock SMS service logs to console. Extensible for:
- Twilio integration
- Notify.lk integration
- Dialog integration

---

## Security Features

### Password Security
- BCrypt hashing (10 rounds)
- Strong password validation (8+ chars, uppercase, lowercase, number, special char)

### JWT Security
- HS256 algorithm
- 24-hour expiration
- Stateless authentication

### Input Validation
- Email format validation
- Phone number validation
- Null/empty checks
- Size limits

### Authorization
- Role-based access control
- Method-level security with @PreAuthorize
- Resource-level ownership checks

---

## Configuration Files

### Backend
- `application.yml` - Main configuration
- `application-dev.yml` - Development overrides
- `.env.local` - Local environment variables

### Frontend
- `.env.local` - API endpoint configuration

### Mobile
- Built-in API configuration

---

## Development Guidelines

### Code Style
- Use meaningful variable and method names
- Keep methods focused and small
- Follow SOLID principles
- Avoid deeply nested logic
- Write self-documenting code

### Layering
- Controllers: Handle HTTP requests only
- Services: Business logic
- Repositories: Database access
- Entities: ORM models
- DTOs: API contracts

### Exception Handling
- Custom exceptions with proper error codes
- Global exception handler
- Meaningful error messages
- Proper HTTP status codes

---

## Testing

### Backend Tests
```bash
mvn test
```

### Frontend Tests
```bash
npm test
```

### API Testing
Use provided Postman collection in `/docs/api-collection.json`

---

## Deployment

### Backend
1. Build: `mvn clean package`
2. Configure production database
3. Set JWT secret in environment
4. Run: `java -jar target/traffic-fine-system-1.0.0.jar`

### Frontend
1. Build: `npm run build`
2. Deploy dist folder to static hosting
3. Configure API endpoint for production

### Mobile
1. Build: `flutter build apk --release`
2. Deploy to Google Play Store

---

## Performance Considerations

### Database
- Indexed columns: email, phone, reference, status
- Lazy loading for relationships
- Pagination for list endpoints

### Caching
- Consider implementing Redis for:
  - Fine lookups by reference
  - User session data
  - Category data

### API Optimization
- Use DTOs to avoid loading unnecessary data
- Implement pagination
- Response compression enabled

---

## Future Enhancements

1. **Payment Gateway Integration**
   - Stripe/PayPal integration
   - Installment plans
   - Automatic reconciliation

2. **Notifications**
   - Real SMS via Twilio/Notify.lk
   - Email notifications
   - Push notifications to mobile app

3. **Features**
   - Fine appeals system
   - License suspension tracking
   - Report generation
   - Batch fine importing

4. **Analytics**
   - Advanced reporting
   - Trend analysis
   - Predictive analytics

5. **Performance**
   - Caching layer (Redis)
   - Message queue (RabbitMQ)
   - Async processing

---

## Support & Contribution

For issues or contributions, follow standard Git workflow:
1. Create feature branch
2. Commit changes with meaningful messages
3. Create pull request
4. Request review

---

## License

This project is part of Sri Lanka Police infrastructure and follows their policies.

---

**Version**: 1.0.0  
**Last Updated**: May 2026

last updtae june 12