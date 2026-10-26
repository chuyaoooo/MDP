# 2. Challenges and assumptions

## 2.1 Key risks

### 1. Emulator limits for attendance
The emulator can't scan a live QR, making attendance (R3) hard to test or demo. Fallbacks: QR images through the virtual camera, or manual code entry for crowded rooms.

### 2. Backend limits: forged or replayed records
Offline check-ins in the Firestore cache could be forged or replayed, giving points to absent students. Cloud Functions revalidate uploads against event state and code windows, reversing invalid records.

### 3. Clock manipulation
Changing the device clock gains nothing: windows follow server time; codes rotate every 10 seconds.

### 4. Location privacy
Location-based check-in would track students beyond events; QR collects no location, removing this risk by scope.

### 5. Time and scope
The list is long: waitlist, clash detection, offline booking, points ledger. We split must-have from nice-to-have ("must" is built in Part 2) and cap own features at two.

## 2.2 Assumptions

- Admins work in the backend, not an app UI.
- Events are single-session and in person; check-in proves presence, not identity.
- Booking works offline through the cache.
- Accounts are created by the university; backend rules enforce roles.
- Points are credited after admin approval; invalid records are reversed.
