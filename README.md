# Network management system

### This is small documentation to my Network management system project
### First of all, I want to emphasize the fact, that I never worked with Kotlin, Spring Boot or TypeScript before
### TypeScript was pretty easy to master, since it is just upgrade on JavaScript, however, with the Spring Boot framework and its vast ecosystem I dealt for the first time, so I hope you will take this into consideration when estimating the project and amount of time I spent on it

## Backend
### Backend part of the application was written in Kotlin using Spring Boot framework
### For the database I used:
- ### In-memory H2 and R2DBC as an API to work with it
- ### Thus, all queries to the database are async
### Reactive streams:
- ### Flux for creating SSE connection
- ### Sinks for emitting events to subscribers
### Backend project structure:
- ### Controllers with two endpoints for subscribing to devices and changing devices` state
- ### DAOs introduce additional logic on top of repositories
- ### DTOs for events data, mapping databases entities and clients` requests
- ### Enums consist of DeviceState enum with available events - INITIAL_STATE, ADDED and REMOVED
- ### Event_bus handles Flux and Sinks related logic - creating Sinks, creating Flux from them, sending events to subscribers etc.
- ### Repositories contain basic set of CRUD functions to work with database
- ### Services incorporate DeviceService, which introduce additional logic on DevicesDao and TopologyService which handles initial state, state updates, and changing device and is directly called from the controller
- ### Topology_db includes database entities themselves and seeder, which populates database with data from JSON
- ### Topology_graph handles creation of graph from devices and connections, also it has DevicesCurrentState class to handle events related to changing devices
  ### In addition, all these components are covered with tests
  ### Also, there are Dockerfile and docker-compose to create Docker container for the project

## Frontend
### Frontend part of the application was written using HTML, CSS and TypeScript
### Frontend project structure: 
- ### index.html, style.css
- ### media folder with cities images to represent particular device
- ### js folder:
  - ### helpers which contains only one functions which is wrapper above ReferenceError
  - ### models contain Device model
  - ### api handles opening and closing SSE connections as part of subscribing to devices and patch request to change devices` state
  - ### app is the main module, which calls functions from other modules in event listeners
  - ### overlay handles overlay with reachable devices, it is some kind of console to track currently available devices
  - ### seeder draws Devices cards of all the devices
  - ### store is used for state management, consists list with current state of the devices, images links list, properties like isConnectionOverlayActive etc.
  - ### tsconfig.json 
  - ### ui module handles all the user interface - popup, overlay, toggling switches etc.