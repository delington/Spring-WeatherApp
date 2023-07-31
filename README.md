# Spring-WeatherApp
Weather Api Spring

## Description

This project is a web application that allows users to enter a city name and view the
current temperature and weather in that city. 

The application is built using 
Spring Boot 3.1.1 and Thymeleaf as the page template engine. It uses JDK 17 and
Retrofit as the HTTP client to interact with external APIs. 

The NominatimClient
API is utilized to obtain the latitude and longitude coordinates of the city,
which are then used to fetch weather data from the Seventimer API.

## Requirements

- Docker

## Setup and Installation

1. Clone the repository to your local machine.

2. Make sure you have Docker installed. If not, download and install it from the official website.

3. Run the application using Docker Compose:

   ```bash
   docker-compose up -d
   ```

4. The application will be accessible at `http://localhost:8080` in your web browser.

## Usage

1. Open the application in your web browser by visiting `http://localhost:8080`.

2. You will see a search box where you can enter the name of the city for which you want to view the weather.

3. After entering the city name, click the "Get Weather" button.

4. The application will use the NominatimClient to obtain the latitude and longitude coordinates of the city.

5. It will then use the Seventimer API to fetch the current weather data based on the coordinates.

6. The weather details, including the temperature and weather conditions, will be displayed on the page.

## APIs Used

- Nominatim API: Used to get the latitude and longitude coordinates of the city based on the city name.
    - API Documentation: [Nominatim API Documentation](https://nominatim.org/release-docs/develop/api/Search/)

- Seventimer API: Used to get the weather data based on the latitude and longitude coordinates.
    - API Documentation: [Seventimer API Documentation](https://www.7timer.info/doc.php)

## Libraries Used

- Thymeleaf: Page template engine used for rendering HTML templates.
    - [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)

- Retrofit: HTTP client used for making API calls.
    - [Retrofit Documentation](https://square.github.io/retrofit/)

## License

This project is licensed under the [MIT License](LICENSE). You are free to use, modify, and distribute the code as per the terms of the license.