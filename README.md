# Hotel Room Allocation

## Overview

The Hotel Room Allocation is a simple API designed to optimize the allocation of hotel rooms between premium and economy classes based on guests willingness to pay. It handles booking requests and maximizes hotel occupancy and revenue.

## Features

- API provides a ```/occupancy``` endpoint for optimizing room occupancy.
- API implements strategy for allocating guests to available room types to achieve the highest possible revenue. 

## Classes

#### BookingController.class
- Processes HTTP requests and enables communication with the application.

#### BookingService.class
- Provides business logic for allocating guests and calculating biggest revenue. 

#### BookingRequest.class
- Represents a received request.
- Contains information such as number of available premium rooms, number of available economy rooms, list of potential guests.
- Provides methods to update scores and get total score.

#### BookingResponse.class
- Represents a application response.
- Contains information such as number of premium guests, premium revenue, number of economy guests, economy revenue.

#### ApplicationExceptionHandler.class
- Provides basic error handling.

## Technologies
- Java 8
- Gradle
- Spring Boot

## How to run
1. Clone the repository:
```java
git clone https://github.com/jbur3/hotel-room-allocation.git
```

2. Build the project:
```java
gradle clean build
```

3. Go to ```HotelRoomAllocation/start-script``` where the ```run.sh``` script is stored.

4. Run ```./run.sh``` command.

## Usage
The HotelRoomAllocation API can be tested with tools like POSTMAN.

### Endpoint
- Optimize occupancy ```POST``` endpoint: ```/occupancy```

### Booking Request
Field | Type         | Description
--- |--------------| ---
```premiumRooms``` | ```int```        | Number of available premium rooms
```economyRooms``` | ```int```        | Number of available economy rooms
```potentialGuests``` | ```List<Double>``` | List of potential guests represented by an array of amounts they are willing to pay

##### Example
```java
{
    "premiumRooms": 1, 
    "economyRooms": 3, 
    "potentialGuests": [23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209]
}
```

### Booking Response
Field | Type               | Description
--- |--------------------| ---
```usagePremium``` | ```int```          | Number of premium rooms used
```revenuePremium``` | ```double```       | Revenue from premium rooms
```usageEconomy``` | ```int``` | Number of economy rooms used
```revenueEconomy``` | ```double``` | Revenue from economy rooms

##### Example
```java
{
    "usagePremium": 1,
    "revenuePremium": 374.0,
    "usageEconomy": 3,
    "revenueEconomy": 167.99
}
```