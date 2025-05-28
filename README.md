# Library Management System API

A backend RESTful API system that allows librarians to manage books, borrowers, and borrowing records.

## Technologies Used

- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- MySQL
- Lombok
- Maven

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven

## Setup

1. Clone the repository
2. Create a MySQL database named `library_db`
3. Update the database credentials in `src/main/resources/application.properties` if needed
4. Run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

### Books

- `POST /api/books` - Create a new book
- `GET /api/books/{id}` - Get a book by ID
- `GET /api/books` - Get all books
- `PUT /api/books/{id}` - Update a book
- `DELETE /api/books/{id}` - Delete a book

### Borrowers

- `POST /api/borrowers` - Create a new borrower
- `GET /api/borrowers/{id}` - Get a borrower by ID
- `GET /api/borrowers` - Get all borrowers
- `PUT /api/borrowers/{id}` - Update a borrower
- `DELETE /api/borrowers/{id}` - Delete a borrower
- `GET /api/borrowers/{id}/books` - Get all books borrowed by a borrower

### Borrowing Records

- `POST /api/borrowing-records` - Create a new borrowing record
- `PUT /api/borrowing-records/{id}/return` - Mark a book as returned

## Sample Requests

### Create a Book
```json
POST /api/books
{
    "title": "The Great Gatsby",
    "author": "F. Scott Fitzgerald",
    "isbn": "978-0743273565",
    "quantity": 5
}
```

### Create a Borrower
```json
POST /api/borrowers
{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+1234567890"
}
```

### Create a Borrowing Record
```json
POST /api/borrowing-records
{
    "borrowerId": 1,
    "bookId": 1
}
```

## Error Handling

The API includes comprehensive error handling for:
- Resource not found (404)
- Bad request (400)
- Validation errors (400)
- Internal server errors (500)

## Features

- CRUD operations for books and borrowers
- Book borrowing and returning functionality
- Automatic quantity management
- Input validation
- Custom error responses
- Transaction management
- RESTful API design 
