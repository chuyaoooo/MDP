# 2. Challenges and assumptions

## 2.1 Key risks

### 1. Emulator limits for attendance
The emulator cannot scan a live QR on a room screen, so without a fallback the attendance flow (R3) cannot be tested or demoed. We agreed two: a QR image through the virtual camera, and manual code entry, which also serves students in crowded rooms.

### 2. Backend limits: forged or replayed offline records
Offline attendance sits in the Firestore cache on the student's own device and could be tampered with or replayed, earning points for someone who did not attend. Cloud Functions revalidate each record on upload against server-side event state and the code window; invalid records are reversed through the points ledger.

### 3. Clock manipulation
Students can change their device clock to check in outside the window. Windows are judged by server time and codes rotate every 10 seconds, so a manipulated clock gains nothing.

### 4. Location privacy
Location-based attendance would track students beyond events. We chose the QR/code deliberately: no location data is collected, removing the risk by scope rather than technical mitigation.

### 5. Time and scope
The feature set — waitlist, clash detection, offline booking, points ledger, admin review — is large for the remaining time. We split requirements into must-have and nice-to-have ("must" means what we build in Part 2) and cap own features at two.

## 2.2 Assumptions

- Admin approval and point adjustments happen in the backend database; the app has no admin UI, as the brief allows.
- Events are single-session and in person; check-in proves presence during the window, not identity beyond the signed-in account.
- Booking works offline through the device cache; a full event is reported on sync.
- Accounts are pre-provisioned by the university; students sign in with a university email, and roles are enforced in backend rules.
- Points are credited after event end and admin approval; invalid records are reversed with an explained notification.
