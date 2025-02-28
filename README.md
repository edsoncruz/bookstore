# Bookstore

To run the application just run BookStoreApplication.class.
It will use HSQLDB in memory and the initial data will be set automatically.

## Endpoints
The endpoints are /books, /bookTypes, /customers, /loyalties, /purchase and /users. All accepts POST, PUT, GET and DELETE.   
Also, the POST /users/register and /users/login to create and login the user.  
The GET /books/available to get only book for selling.  
The POST /purchase/order to create a purchase:
```json
{
    "customerId": 9,
    "books": [
        {
            "id":1,
            "amount":1
        },
        {
            "id":2,
            "amount":1

        },
        {
            "id":3,
            "amount":1
        }
    ]
}
```

## How it works
I created layer application with Controller, Services, Repository and Entities.  
I used Java 21, Spring Boot, Spring Web, Spring Security, JPA, JWT and HSQLDB.  
I wish I had more time to do better and cover more details.

