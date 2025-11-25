# Campus360

## Introduction
Campus360 is an Android-based indoor navigation app designed for students, faculty, and visitors at Blekinge Institute of Technology (BTH). Traditional navigation tools like Google Maps and OpenStreetMap do not support indoor routing, leading to confusion for new visitors once they enter campus buildings.

Campus360 provides a simple, offline-first, and student-friendly solution that allows users to:

- Search for rooms, labs, offices, and facilities  
- View floor numbers and the nearest recommended entrance  
- Explore Points of Interest (POIs)  
- Navigate using clean, simplified indoor maps  
- Access basic guidance from entrance → floor → room  

> Campus360 is not meant to be a full indoor GPS but a lightweight helper tool for campus orientation.


---

## Architecture Overview
<img width="1024" height="1024" alt="1764079651292" src="https://github.com/user-attachments/assets/00b9d5cc-b3d8-4e68-a156-4cd8b279ce2b" />

---

## User Stories

| # | As a… | I want/need to… | So that… | Acceptance Criteria |
|---|-------|------------------|-----------|----------------------|
| US1 | User | Save frequently visited locations | Access them quickly | Favorites list, toggle, detail view |
| US2 | User | Enable accessibility mode | Navigate comfortably | High-contrast UI, voice cues |
| US3 | User | View real-time crowdedness | Choose best time to visit | Crowdedness indicator + updates |
| US4 | User | View indoor room temperature | Choose a comfortable place | Temp display, fallback message |
| US5 | User | Request accessible routes | Avoid stairs/barriers | Accessible routing toggle |
| US6 | User | Receive room-closure notifications | Avoid unavailable rooms | Push alert + link to location |
| US7 | User | Plan multi-stop routes | Move efficiently | Multi-stop UI, reorder, optimized path |
| US8 | User | Download offline campus maps | Navigate without internet | Offline mode toggle + indicator |
| US9 | User | Report inaccurate map data | Keep information updated | Report form + confirmation |
| US10 | User | Get reminders to leave for events | Arrive on time | Travel-time-based notifications |

---
### Development Status

Track the development status of the app.

Use
[ ] for not implemented
[x] for implemented.

[ ] US1

[ ] US2

[ ] US3

[ ] US4

[ ] US5

[ ] US6

[ ] US7

[ ] US8

[ ] US9

[ ] US10
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

