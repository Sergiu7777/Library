Sample CRUD application for Library.
Use this sample endpoints to test the app:

1. Find and borrow a book named "Refactoring" by the user with id = 1:
   GET /api/books/borrow?userId=1&searchString=Refactoring
   Response body:
   {
  "id": 5,
  "title": "Refactoring",
  "author": "Martin Fowler",
  "isbn": "9780201485677",
  "amount": 0
}

2. Add a new book to the library:
   POST /api/books
   Request body:
  {
    "title": "White Fang",
    "author": "Jack London",
    "isbn": "9780132555784",
    "amount": 7
  }

  Response body:
  {
    "id": 6,
    "title": "White Fang",
    "author": "Jack London",
    "isbn": "9780132555784",
    "amount": 7
  }
   
3. List all my borrowed books:
   GET /api/books/borrow?userId=1&pageNumber=1&pageSize=5
     Response body:
  [
  {
    "id": 1,
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "9780132350884",
    "amount": 3
  },
  {
    "id": 4,
    "title": "The Pragmatic Programmer",
    "author": "Andy Hunt",
    "isbn": "9780201616224",
    "amount": 4
  }
]
   
4. Return a borrowed book and update the book stock:
   POST /api/books/return?userId=1
   Request body:
   {
    "id": 1,
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "9780132350884",
    "amount": 3
  }

  Response body:
  {
    "id": 1,
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "9780132350884",
    "amount": 4
}
  


   
