# ShowImages
Image management and slideshow application using Spring Boot.

## Description
ShowImages is an application that allows you to:
- Add and remove images.
- Manage slideshows, link them to images.
- Record and analyze Proof of Play data.

## Technology stack:
- **Backend:** Spring Boot, Java 17
- **Database:** PostgreSQL
- **API:** RESTful

## Requirements
- Java 17
- Maven 3.8+
- Docker и Docker Compose

## Clone the repository:
   ```bash
   git clone https://github.com/privetEdik/ShowImages.git
   cd ShowImages
   ```

## Configuration
Before running, create a `.env` file in the root of the project and add the following parameters:
   ```env
   POSTGRES_USERNAME=postgres
   POSTGRES_PASSWORD=postgres
   ```
## Run the application via Docker:
   ```bash
   docker-compose -f docker-compose-prod.yml up --build
   ```
## Usage
API endpoints:

### 1. Adding a first image
- **URL:** `POST http://localhost:8080/images`
- **Request Body:**
  ```json
  {
    "url": "https://example.com/image1.jpg",
    "duration": 5
  }
  ```
### 2. Adding a second image
- **URL:** `POST http://localhost:8080/images`
- **Request Body:**
  ```json
  {
    "url": "https://example.com/image2.jpg",
    "duration": 5
  }
  ```
### 3. Adding a third image
- **URL:** `POST http://localhost:8080/images`
- **Request Body:**
  ```json
  {
    "url": "https://example.com/image3.jpg",
    "duration": 5
  }
  ```
### 4. Adding a slideshow. Execute twice.
- **URL:** `POST http://localhost:8080/slideshow`
- **Request Body:**
  ```json
   [
       {
           "url": "http://example.com/image1.jpg",
           "duration": 5
       },
       {
           "url": "http://example.com/image2.jpg",
           "duration": 5
       },
       {
           "url": "http://example.com/image3.jpg",
           "duration": 5
       }
   ]
  ```
### 5. Getting a slide show with images ordered by date added.
- **URL:** `GET http://localhost:8080/slideshow/1/order`

### 6. Search for images and their associated slideshows using keywords from the URL or duration.
- **URL:** `GET http://localhost:8080/images/search?keyword=e&duration=`

### 7. Record an event when an image is replaced by the next one.
- **URL:** `POST http://localhost:8080/slideshow/1/proof-of-play/1`
  will be displayed in the logs

### 8. Remove an image by its ID.
- **URL:** `DELETE http://localhost:8080/images/1`

### 8. Remove a slideshow by its ID.
- **URL:** `DELETE http://localhost:8080/slideshow/1`

## Author
- **Name:** Edward
- **Email:** eduard3236volkov@gmail.com
- **GitHub:** https://github.com/privetEdik