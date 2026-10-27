# 3. Technical Design — Architecture and Data

## 3.1 System Architecture

The Kotlin Android app uses MVVM: student and club interfaces observe ViewModels that call repositories. Pre-provisioned university accounts use Firebase Authentication; server-assigned roles control navigation. Firestore stores shared data and queues offline writes; Cloud Functions validates bookings, attendance and points. Clients submit requests but cannot directly write verified attendance or points. Administrators approve events through protected backend operations. **Drawbacks:** MVVM adds boilerplate compared with direct UI calls; Firebase creates vendor dependence; Firestore persistence offers less retry control than a separate Room/WorkManager queue.

**Figure 1 — System Architecture**

```mermaid
%%{init: {"theme":"base","themeVariables":{"background":"#ffffff","primaryColor":"#ffffff","primaryTextColor":"#000000","primaryBorderColor":"#000000","secondaryColor":"#ffffff","tertiaryColor":"#ffffff","lineColor":"#000000","textColor":"#000000","edgeLabelBackground":"#ffffff"}}}%%
flowchart TD
    AUTH["Firebase Authentication"] --> ROLE{"Backend role"}
    ROLE -->|Student| S["Student UI"]
    ROLE -->|Club| C["Club UI"]
    S --> VM["ViewModel"]
    C --> VM
    VM --> R["Repository"]
    R --> SDK["Firestore SDK"]
    SDK <--> LOCAL["Offline cache and pending writes"]
    SDK <--> DB["Cloud Firestore"]
    DB --> FN["Cloud Functions: validation and points"]
    FN --> DB
    ADMIN["Protected admin operations"] --> FN
    RULES["Firestore Security Rules"] -.-> DB
    classDef default fill:#ffffff,stroke:#000000,color:#000000,stroke-width:1.5px;
    linkStyle default stroke:#000000,stroke-width:1.5px;
```

## 3.2 Data Model

Six collections support the app: Users, Clubs, Events, Bookings, Attendance and PointsLedger. Events follow Draft → Pending → Approved/Rejected → Completed/Cancelled; clubs cannot edit approved events, and cancellations need administrator approval. Bookings can be Pending, Confirmed, Waitlisted, Cancelled or Rejected; waitlisted students confirm offers. Server transactions check capacity and time clashes atomically, including competition for the final available place. Attendance keeps unique requests and verification results. An append-only ledger records credits and reversals. **Drawbacks:** Transactions require server access, unlike unreliable client-only checks; ledger entries use more storage than directly editing a points total but support auditing.

**Figure 2 — Entity Relationship Diagram**

```mermaid
%%{init: {"theme":"base","themeVariables":{"background":"#ffffff","primaryColor":"#ffffff","primaryTextColor":"#000000","primaryBorderColor":"#000000","secondaryColor":"#ffffff","tertiaryColor":"#ffffff","lineColor":"#000000","textColor":"#000000","edgeLabelBackground":"#ffffff"}}}%%
erDiagram
    USERS ||--o{ CLUBS : manages
    CLUBS ||--o{ EVENTS : creates
    USERS ||--o{ BOOKINGS : makes
    EVENTS ||--o{ BOOKINGS : receives
    USERS ||--o{ ATTENDANCE : submits
    EVENTS ||--o{ ATTENDANCE : records
    ATTENDANCE ||--o{ POINTS_LEDGER : references
    USERS ||--o{ POINTS_LEDGER : earns
    USERS {
        string userId PK
        string role
    }
    CLUBS {
        string clubId PK
        string managerId FK
    }
    EVENTS {
        string eventId PK
        string clubId FK
        string status
        int capacity
        int approvedPoints
    }
    BOOKINGS {
        string bookingId PK
        string studentId FK
        string eventId FK
        string status
    }
    ATTENDANCE {
        string checkInId PK
        string studentId FK
        string eventId FK
        string status
    }
    POINTS_LEDGER {
        string transactionId PK
        string attendanceId FK
        int delta
    }

```

**Figure 2b — Event Status Flow**

```mermaid
%%{init: {"theme":"base","themeVariables":{"background":"#ffffff","primaryColor":"#ffffff","primaryTextColor":"#000000","primaryBorderColor":"#000000","secondaryColor":"#ffffff","tertiaryColor":"#ffffff","lineColor":"#000000","textColor":"#000000","edgeLabelBackground":"#ffffff"}}}%%
flowchart LR
    D["Draft"] --> P["Pending"]
    P -->|Approve| A["Approved"]
    P -->|Reject| R["Rejected"]
    A -->|Event ends| C["Completed"]
    A -->|Admin approves cancellation| X["Cancelled"]
    R --> N["New application required"]
    classDef default fill:#ffffff,stroke:#000000,color:#000000,stroke-width:1.5px;
    linkStyle default stroke:#000000,stroke-width:1.5px;
```

## 3.6 Points and Role Enforcement

Points are credited immediately after server-verified check-in; offline claims receive points only after validation. Invalid awards generate reversing ledger entries and notifications. Authentication, Security Rules and backend role checks restrict privileges. **Drawback:** Pre-provisioning accounts takes administrative effort, but self-selected roles and client-calculated points are insecure.
