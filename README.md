# Viala - Household Medicine Inventory

This is a backend application that provides an API for managing a household medicine inventory.

## Technologies

- Java 17
- Spring Boot 3.1.5
- Maven
- Lombok
- Hibernate
- PostgreSQL

## Database

We have chosen PostgreSQL as the database for this project. Here's why:

- **Relational Data:** The data we are storing (Users, Medications, Sharing) is highly relational. PostgreSQL is a powerful relational database that excels at managing this type of data.
- **Data Integrity:** We need to enforce data integrity, for example, by using foreign keys to link medications to users. PostgreSQL provides strong support for referential integrity.
- **Scalability:** While MongoDB is known for its scalability, PostgreSQL is also highly scalable and can handle large amounts of data.
- **JSON Support:** PostgreSQL has excellent support for JSONB, which allows us to store semi-structured data like allergies in a flexible way.

## API Documentation

The API is documented using the OpenAPI 3.0 specification. You can find the specification in the `openapi.yaml` file.

### Authentication

All endpoints except for `/api/users/register` and `/api/auth/login` require authentication. We are using JWT for authentication.

To authenticate, you need to send a POST request to `/api/auth/login` with the following body:

```json
{
  "username": "your-username",
  "password": "your-password"
}
```

The response will contain a JWT token that you need to include in the `Authorization` header of all subsequent requests.

```
Authorization: Bearer <your-jwt-token>
```

### API Endpoints

#### User Profile Management

- `POST /api/users/register`: Register a new user.
- `GET /api/users/{id}`: Get user profile information.
- `PUT /api/users/{id}`: Update user profile information.

#### Medication Management

- `POST /api/medications`: Add a new medication.
- `GET /api/medications`: Get a list of all medications.
- `GET /api/medications/{id}`: Get a specific medication.
- `PUT /api/medications/{id}`: Update a medication.
- `DELETE /api/medications/{id}`: Delete a medication.
- `PUT /api/medications/{id}/quantity`: Update the quantity of a medication.

#### Sharing Functionality

- `POST /api/users/share`: Share a medication list with another user.

#### OpenAI Integration

- `POST /api/openai/check-compatibility`: Check the compatibility of a list of drugs.
- `POST /api/openai/select-drug`: Get a drug selection for a given symptom.
