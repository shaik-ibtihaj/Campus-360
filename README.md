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


---

## Targeted Users

Campus360 is designed for a diverse set of users including students, faculty, administrative staff, and visitors.

### Persona 1 — Rahul Gandhi (19)
- First-year international student  
- Needs help locating classrooms  
- Workflow: Opens app → searches “J305” → views map → reaches destination confidently  

### Persona 2 — Babar Azam (35)
- Visiting lecturer  
- Needs efficient navigation inside the building  
- Workflow: Opens app → browses POIs → selects Auditorium → follows recommended route  

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

## App Context & Data Management

Campus360 operates **entirely offline**.  
All resources are stored locally, ensuring privacy and responsiveness.

### System Overview
- User searches for a room or POI  
- App retrieves data from bundled JSON files  
- Floorplan images are displayed  
- Rooms or POIs are highlighted  
- Basic step-wise navigation is shown  

### Target Hardware Requirements
- Android smartphones (5–7 inch screens recommended)  
- Minimum OS: **Android 9 (API 28)**  
- Input: Tap, pan, pinch-to-zoom  

### Data Collected  
All data stays on the device:
- Search queries  
- Selected POIs/rooms  
- User preferences (favorites, accessibility mode)  

### Data Sources  
- JSON files (rooms, POIs, routing data)  
- Indoor map images (PNG/JPG)  
- User inputs  

### Data Lifecycle  
- Temporary session data cleared on app exit  
- Persistent local data only for user preferences  
- No cloud storage or external communication  

---

## Minimal Viable Product (MVP)

The MVP includes:

- Room search  
- POI browsing  
- Room details screen  
- Interactive floorplan viewer (zoom/pan)  
- Marker highlighting selected room  
- Basic navigation instructions  

---

## Development Plan

| Phase | Timeline | Tasks |
|-------|----------|--------|
| P1 | Week 1–2 | Requirements, floorplan design, UI wireframes |
| P2 | Week 2–3 | Project setup, JSON integration, base activities |
| P3 | Week 3–4 | Search, POI categories, details view |
| P4 | Week 4–5 | Floorplan viewer, marker placement |
| P5 | Week 5–6 | Basic directions implementation |
| P6 | Week 6–7 | Testing, debugging, polishing |

---

## Development Risks

### Technical Risks
- Incorrect room positions  
- Misaligned floor plans  
- Gesture handling issues (zoom/pan)  

### Data Risks
- Missing or incorrect room/POI data  
- Inconsistent floor map design  

### Project Risks
- Limited time for full testing  
- Uneven team workload  

### Mitigation
- Early data validation  
- Frequent integration testing  
- Weekly team sync meetings  

---

## How to Use
(To be updated after UI finalization)

---

## Build Instructions

### Build (Gradle)
```bash
./gradlew clean assembleDebug

