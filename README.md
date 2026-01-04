# Campus360

## Introduction
Campus360 is an Android-based indoor navigation app designed for students, faculty, and visitors at Blekinge Institute of Technology (BTH). Traditional navigation tools like Google Maps and OpenStreetMap do not support indoor routing, leading to confusion for new visitors once they enter campus buildings.

Campus360 provides a simple, offline-first, and student-friendly solution that allows users to:

- Search for rooms, labs, and offices  
- View floor numbers and the nearest recommended entrance  
- Explore Points of Interest (POIs)  
- Navigate using clean, simplified indoor maps  
- Access basic guidance  

> Campus360 is not meant to be a full indoor GPS but a lightweight helper tool for campus orientation.


---

## Architecture Overview
<img width="3600" height="4800" alt="Campus360_Architecture_Diagram" src="https://github.com/user-attachments/assets/0d5b906c-be98-4eab-b00e-5486e9730c43" />


---

## User Stories![Uploading Campus360_Architecture_Diagram.png…]()


| # | As a… | I want/need to… | So that… | Acceptance Criteria |
|---|-------|------------------|-----------|----------------------|
| US1 | User | Search for a classroom or lecture hall by name or room number | Reach the correct location without confusion | Matching locations are listed; selecting a result opens details |
| US2 | User | Browse locations based on their category or type | Get an overview of available rooms and facilities | Categories are browsable; selecting a category shows results; selecting an item opens its details |
| US3 | User | Follow visual, step-by-step navigation instructions | Move confidently in the correct direction | A shortest path is computed; the map highlights the route; turn-by-turn guidance is displayed |
| US4 | User | Explore campus points of interest such as libraries or cafeterias | Make better use of campus facilities | POIs are accessible via search and categories; details include information and a route option |
| US5 | User | Manually select a starting location | Plan a route from any chosen point | A start-location picker is available; the selected start is used for route calculation |
| US6 | User | View destination details before starting navigation | Confirm the selected destination is correct | Details screen shows name, category, description, and actions like “View on Map” and “Start Route” |
| US7 | User | Save frequently visited locations as favorites | Access important places quickly | Favorites section is available; locations can be added or removed; selecting a favorite opens its details |
| US8 | User | Report navigation issues or campus inconveniences | Help improve map accuracy and user experience | Report option is available; issue details can be submitted via mail |


---
### Development Status

Track the development status of the app.

Use
[ ] for not implemented
[x] for implemented.

[x] US1

[x] US2

[x] US3

[x] US4

[x] US5

[x] US6

[x] US7

[x] US8


---
## How to Use
  To be upadated in future
### Build

Using Gradle (from project root):
./gradlew clean assembleDebug
The APK is generated under `app/build/outputs/apk/debug/app-debug.apk`.

### Test

./gradlew test --rerun-tasks

### Run

Android Studio
- Open the project in Android Studio.
- Select a device/emulator and click Run.

